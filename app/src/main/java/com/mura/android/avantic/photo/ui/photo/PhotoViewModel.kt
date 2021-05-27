package com.mura.android.avantic.photo.ui.photo

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.mura.android.avantic.photo.data.mapper.PhotoMapperToDB
import com.mura.android.avantic.photo.data.response.ResponsePhoto
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.mura.android.avantic.photo.domain.usecase.DeletePhotoUseCase
import com.mura.android.avantic.photo.domain.usecase.GetPhotoUseCase
import com.mura.android.avantic.photo.domain.usecase.InsertPhotoUseCase
import com.mura.android.avantic.photo.domain.usecase.UpdatePhotoCaseUse
import com.mura.android.avantic.utils.SavePhotoUtil
import com.mura.android.avantic.utils.extentions.NetworkHelper
import com.mura.android.avantic.utils.response.ResultManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class

PhotoViewModel @Inject constructor(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val insertPhotoUseCase: InsertPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val updatePhotoCaseUse: UpdatePhotoCaseUse,
    private val networkHelper: NetworkHelper,
    private val appContext: Application
) : ViewModel() {

    private val _photo = MutableLiveData<PhotoData>()
    val photoData: LiveData<PhotoData>
        get() = _photo

    private val _photoList = MutableLiveData<List<PhotoData>>()
    val photoDataList: LiveData<List<PhotoData>>
        get() = _photoList

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var job: Job? = null

    fun requestPhotos() {
        job = viewModelScope.launch {
            when (val result = getPhotoUseCase.getPhotos(networkHelper.isNetworkConnected())) {
                is ResultManager.Success -> {
                    val ids = result.items as? Array<*>
                    if (ids != null) {
                        saveAllPhotosInDB(result.items.filterIsInstance<PhotoData>())
                    }
                }
                is ResultManager.Error -> {
                    _error.postValue(result.errorMessage)
                }
                else -> {
                    _error.postValue("Error!")
                }
            }
            _isLoading.value = false
        }
    }

    private fun saveAllPhotosInDB(list: List<PhotoData>) {
        job = viewModelScope.launch {
            insertPhotoUseCase.saveAllPhotosInDB(list)
            getPhotonFromDB()
        }
    }

    fun createNewPhotoInApi(bitmap: Bitmap?, responsePhoto: ResponsePhoto) {
        _isLoading.value = true
        job = viewModelScope.launch {
            if (bitmap != null) {
                val uri =
                    SavePhotoUtil.saveBitmap(appContext, bitmap, "${System.currentTimeMillis()}")
                responsePhoto.url = uri.toString()
                responsePhoto.thumbnailUrl = uri.toString()
            }
            when (val result = insertPhotoUseCase.createNewPhotoInApi(
                networkHelper.isNetworkConnected(),
                responsePhoto
            )) {
                is ResultManager.Success -> {
                    createNewPhotoInDB(result.items as PhotoData)
                }
                is ResultManager.Error -> {
                    _isLoading.value = false
                    _error.postValue(result.errorMessage)
                }
                else -> {
                    _isLoading.value = false
                    _error.postValue("Error!")
                }
            }
        }
    }

    private fun createNewPhotoInDB(photoData: PhotoData) {
        job = viewModelScope.launch {
            insertPhotoUseCase.saveAllPhotosInDB(listOf(photoData))
            getPhotonFromDB()
        }
    }

    fun updateTilePhotoFromApi(photoData: ResponsePhoto) {
        _isLoading.value = true
        job = viewModelScope.launch {
            when (val result = updatePhotoCaseUse.updatePhotoInApi(
                networkHelper.isNetworkConnected(),
                photoData
            )) {
                is ResultManager.Success -> {
                    updateTitlePhotoFromDB(PhotoMapperToDB().reverseMap(photoData))
                }
                is ResultManager.Error -> {
                    _isLoading.value = false
                    _error.postValue(result.errorMessage)
                }
                else -> {
                    _isLoading.value = false
                    _error.postValue("Error!")
                }
            }
        }
    }

    private fun updateTitlePhotoFromDB(photoData: PhotoData) {
        job = viewModelScope.launch {
            updatePhotoCaseUse.updatePhotoInDB(photoData)
            getPhotonFromDB()
        }
    }

    fun deletePhotoByIdInApi(id: String) {
        _isLoading.value = true
        job = viewModelScope.launch {
            when (val result =
                deletePhotoUseCase.deletePhotoInApi(networkHelper.isNetworkConnected(), id)) {
                is ResultManager.Success -> {
                    deletePhotoInDB(id)
                }
                is ResultManager.Error -> {
                    _isLoading.value = false
                    _error.postValue(result.errorMessage)
                }
                else -> {
                    _isLoading.value = false
                    _error.postValue("Error!")
                }
            }
        }
    }

    private fun deletePhotoInDB(id: String) {
        job = viewModelScope.launch {
            deletePhotoUseCase.deletePhotoInDB(id)
            getPhotonFromDB()
        }
    }

    private fun getPhotonFromDB() {
        job = viewModelScope.launch {
            when (val result = getPhotoUseCase.getPhotosFromBD()) {
                is ResultManager.Success -> {
                    if (result.items is List<*>) {
                        _photoList.postValue(result.items.filterIsInstance<PhotoData>())
                    }
                }
                is ResultManager.Error -> {
                    _error.postValue(result.errorMessage)
                }
                else -> {
                    _error.postValue("Error!")
                }
            }
            _isLoading.value = false
        }
    }
}