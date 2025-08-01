package com.example.evention.ui.screens.profile.user.userEdit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserEditViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.userRemoteDataSource

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _editSuccess = MutableStateFlow(false)
    val editSuccess: StateFlow<Boolean> = _editSuccess

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    fun setSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun editUser(context: Context, userId: String, username: String, email: String, phone: Int) {
        viewModelScope.launch {
            try {
                val imagePart = selectedImageUri.value?.let { uri ->
                    val inputStream = context.contentResolver.openInputStream(uri)!!
                    val fileBytes = inputStream.readBytes()
                    inputStream.close()

                    val fileName = "profile_${System.currentTimeMillis()}.jpg"
                    val requestFile = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("profilePicture", fileName, requestFile)
                }

                remoteDataSource.updateUser(
                    userId = userId,
                    username = username,
                    email = email,
                    phone = phone,
                    profilePicture = imagePart
                )

                _editSuccess.value = true
            } catch (e: Exception) {
                _editSuccess.value = false
            }
        }
    }

    fun clearEditSuccess() {
        _editSuccess.value = false
    }
}
