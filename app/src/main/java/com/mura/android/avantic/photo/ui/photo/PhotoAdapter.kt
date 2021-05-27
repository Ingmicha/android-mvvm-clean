package com.mura.android.avantic.photo.ui.photo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.mura.android.avantic.R
import com.mura.android.avantic.databinding.ItemPhotoBinding
import com.mura.android.avantic.photo.data.response.ResponsePhoto
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.squareup.picasso.Picasso

class PhotoAdapter(val context: Context, private var photoViewModel: PhotoViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var photosList: List<PhotoData>

    class ItemViewHolder(var viewBinding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    fun updateDate(photoDataList: List<PhotoData>) {
        this.photosList = photoDataList.reversed()
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        itemViewHolder.viewBinding.apply {
            titleTextView.text =
                context.resources.getString(R.string.text_title, photosList[position].id.toString())
            secondaryTextView.text =
                photosList[position].title
            Picasso
                .get()
                .load(photosList[position].url)
                .into(photoAppCompatImageView)


            deleteMaterialButton.setOnClickListener {
                onShowDialogForDelete(photosList[position])
            }
            editMaterialButton.setOnClickListener {
                onShowDialogForEdit(photosList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (this::photosList.isInitialized)
            photosList.size
        else
            0
    }

    private fun onShowDialogForDelete(photoData: PhotoData) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.resources.getString(R.string.delete_dialog_title))
            .setMessage(context.resources.getString(R.string.delete_dialog_message))
            .setNegativeButton(context.resources.getString(R.string.decline_bottom)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(context.resources.getString(R.string.delete_bottom)) { dialog, which ->
                photoViewModel.deletePhotoByIdInApi(photoData.id.toString())
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun onShowDialogForEdit(photoData: PhotoData) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.resources.getString(R.string.edit_dialog_title))
            .setNegativeButton(context.resources.getString(R.string.decline_bottom)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(context.resources.getString(R.string.edit_bottom)) { dialog, which ->
                val newTitle =
                    (dialog as? AlertDialog)?.findViewById<TextInputEditText>(R.id.title_edit_text)!!.text.toString()
                photoData.title = newTitle
                photoViewModel.updateTilePhotoFromApi(
                    ResponsePhoto(
                        id = photoData.id.toString(),
                        title = photoData.title
                    )
                )
            }
            .setView(R.layout.layout_edit_dialog)
            .setCancelable(false)
            .show()
    }
}