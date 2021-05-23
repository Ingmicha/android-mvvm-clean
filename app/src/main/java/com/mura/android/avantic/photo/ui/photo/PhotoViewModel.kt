package com.mura.android.avantic.photo.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mura.android.avantic.photo.domain.model.Photo
import com.mura.android.avantic.photo.domain.usecase.DeletePhotoUseCase
import com.mura.android.avantic.photo.domain.usecase.GetPhotoUseCase
import com.mura.android.avantic.photo.domain.usecase.InsertPhotoUseCase
import com.mura.android.avantic.photo.domain.usecase.UpdatePhotoCaseUse
import com.mura.android.avantic.utils.extentions.NetworkHelper
import com.mura.android.avantic.utils.extentions.NetworkHelper.Companion.NO_CONNECTED
import com.mura.android.avantic.utils.response.ResultManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val insertPhotoUseCase: InsertPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val updatePhotoCaseUse: UpdatePhotoCaseUse,
    private val networkHelper: NetworkHelper,
    private val gson: Gson
) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo>
        get() = _photo

    private val _photoList = MutableLiveData<List<Photo>>()
    val photoList: LiveData<List<Photo>>
        get() = _photoList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var job: Job? = null

    fun getPhotoFromApi() {
        _isLoading.value = true
        if (networkHelper.isNetworkConnected()) {
            job = viewModelScope.launch {
                when (val result = getPhotoUseCase.getPhotoFromApi()) {
                    is ResultManager.Success -> {
                        val list = gson.fromJson(
                            result.items!!.body(),
                            Array<Photo>::class.java
                        ).toList()
                        saveAllPhotosInDB(list)
                        _isLoading.value = false
                    }
                    is ResultManager.Error -> {
                        _error.postValue(result.errorMessage)
                    }
                    else -> {
                        _error.postValue("Error!")
                    }
                }
            }
        } else {
            getPhotonFromDB()
        }
    }

    private fun saveAllPhotosInDB(list: List<Photo>) {
        job = viewModelScope.launch {
            insertPhotoUseCase.saveAllPhotosInDB(list)
            getPhotonFromDB()
        }
    }

    fun createNewPhotoInApi(photo: Photo) {
        _isLoading.value = true
        if (networkHelper.isNetworkConnected()) {
            job = viewModelScope.launch {
                when (val result = insertPhotoUseCase.createNewPhotoInApi(photo)) {
                    is ResultManager.Success -> {
                        createNewPhotoInDB(photo)
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
        } else {
            _error.postValue(NO_CONNECTED)
        }
    }

    private fun createNewPhotoInDB(photo: Photo) {
        job = viewModelScope.launch {
            insertPhotoUseCase.saveAllPhotosInDB(listOf(photo))
            getPhotonFromDB()
        }
    }

    fun updateTilePhotoFromApi(photo: Photo) {
        _isLoading.value = true
        if (networkHelper.isNetworkConnected()) {
            job = viewModelScope.launch {
                when (val result = updatePhotoCaseUse.updatePhotoInApi(photo)) {
                    is ResultManager.Success -> {
                        updateTitlePhotoFromDB(photo)
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
        } else {
            _error.postValue(NO_CONNECTED)
        }
    }

    private fun updateTitlePhotoFromDB(photo: Photo) {
        job = viewModelScope.launch {
            updatePhotoCaseUse.updatePhotoInDB(photo)
            getPhotonFromDB()
        }
    }

    fun deletePhotoByIdInApi(id: String) {
        _isLoading.value = true
        if (networkHelper.isNetworkConnected()) {
            job = viewModelScope.launch {
                when (val result = deletePhotoUseCase.deletePhotoInApi(id)) {
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
        } else {
            _error.postValue(NO_CONNECTED)
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
            when (val result = getPhotoUseCase.getPhotoFromDB()) {
                is ResultManager.Success -> {
                    _photoList.postValue(result.items!!)
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