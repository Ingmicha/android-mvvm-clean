package com.mura.android.avantic.photo.ui.photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.mura.android.avantic.R
import com.mura.android.avantic.databinding.ActivityPhotoBinding
import com.mura.android.avantic.photo.domain.model.Photo
import com.mura.android.avantic.utils.SavePhotoUtil
import com.mura.android.avantic.utils.extentions.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.properties.Delegates


@AndroidEntryPoint
class PhotoActivity : AppCompatActivity() {

    private val photoViewModel: PhotoViewModel by viewModels()
    private lateinit var binding: ActivityPhotoBinding
    private lateinit var adapter: PhotoAdapter
    private lateinit var permission: String

    private var code by Delegates.notNull<Int>()
    private var isFromCamera by Delegates.notNull<Boolean>()

    private val registerLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                if (isFromCamera) {
                    val imageBitmap = result.data!!.extras!!.get("data") as Bitmap
                    val uri = SavePhotoUtil.saveBitmap(
                        this,
                        imageBitmap,
                        "${System.currentTimeMillis()}"
                    )
                    onShowDialogForNewPhoto(uri.toString())
                } else {
                    val intent: Intent? = result.data
                    intent ?: return@registerForActivityResult
                    onShowDialogForNewPhoto(intent.data!!.toString())
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo)
        binding.apply {
            lifecycleOwner = this.lifecycleOwner
        }
        setToolbar()
        setRecyclerView()
        setObservers()
        setData()
    }

    private fun setToolbar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_photo_from_gallery -> {
                    openGallery()
                    true
                }
                R.id.add_photo_from_camera -> {
                    openCamera()
                    true
                }
                else -> false
            }
        }
    }

    private fun setRecyclerView() {
        adapter = PhotoAdapter(this, photoViewModel)
        adapter.notifyDataSetChanged()

        binding.recyclerViewLyricList.adapter = this.adapter
        binding.recyclerViewLyricList.setHasFixedSize(true)

    }

    private fun setObservers() {

        observe(photoViewModel.isLoading) {
            it ?: return@observe
            binding.progressCircular.visibility = if (it) View.VISIBLE else View.GONE
        }

        observe(photoViewModel.photoList) {
            it ?: return@observe
            binding.recyclerViewLyricList.visibility = View.VISIBLE
            adapter.updateDate(it)
        }

        observe(photoViewModel.error) {
            it ?: return@observe
            toast(it)
        }
    }

    private fun setData() {
        photoViewModel.getPhotoFromApi()
    }

    private fun openGallery() {
        permission = PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_NAME
        code = PERMISSION_REQUEST_READ_EXTERNAL_CODE
        checkPermission()
    }

    private fun openCamera() {
        if (hasCamera()) {
            permission = PERMISSION_REQUEST_CAMERA_NAME
            code = PERMISSION_REQUEST_CAMERA_CODE
            checkPermission()
        }
    }

    private fun dispatchTakePictureIntent() {
        isFromCamera = true
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            registerLaunch.launch(takePictureIntent)
        }
    }

    private fun dispatchOpenGalleryIntent() {
        isFromCamera = false
        Intent(ACTION_PICK).apply {
            type = "image/*"
        }.also { takePictureIntent ->
            registerLaunch.launch(takePictureIntent)
        }
    }

    private fun onShowDialogForNewPhoto(url: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.new_photo_dialog_title))
            .setNegativeButton(resources.getString(R.string.decline_bottom)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.save_bottom)) { dialog, which ->
                val newTitle =
                    (dialog as? AlertDialog)?.findViewById<TextInputEditText>(R.id.title_edit_text)?.text.toString()

                photoViewModel.createNewPhotoInApi(
                    Photo(
                        title = newTitle,
                        url = url,
                        thumbnailUrl = url,
                        albumId = (0..100).random().toString()
                    )
                )
            }
            .setView(R.layout.layout_edit_dialog)
            .show()

    }

    private fun checkPermission() {
        if (checkSelfPermissionCompat(Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            when (permission) {
                PERMISSION_REQUEST_CAMERA_NAME -> {
                    dispatchTakePictureIntent()
                }
                PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_NAME -> {
                    dispatchOpenGalleryIntent()
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (shouldShowRequestPermissionRationaleCompat(permission)) {
            requestPermissionsCompat(
                arrayOf(permission),
                code
            )

        } else {
            requestPermissionsCompat(arrayOf(permission), code)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CAMERA_CODE -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent()
                } else {
                    requestPermission()
                }
            }
            PERMISSION_REQUEST_READ_EXTERNAL_CODE -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchOpenGalleryIntent()
                } else {
                    requestPermission()
                }
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CAMERA_CODE = 0
        const val PERMISSION_REQUEST_READ_EXTERNAL_CODE = 1
        const val PERMISSION_REQUEST_CAMERA_NAME = Manifest.permission.CAMERA
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_NAME =
            Manifest.permission.READ_EXTERNAL_STORAGE
    }
}