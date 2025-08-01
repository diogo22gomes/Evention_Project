package com.example.evention.ui.screens.profile.admin.editEvent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditEventViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.eventRemoteDataSource

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event

    private val _editSuccess = MutableStateFlow(false)
    val editSuccess: StateFlow<Boolean> = _editSuccess

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    fun setSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun editEvent(
        context: Context,
        eventId: String,
        name: String,
        description: String,
        startAt: String,
        endAt: String,
        price: Float,
    ) {
        viewModelScope.launch {
            try {
                val imagePart = selectedImageUri.value?.let { uri ->
                    val inputStream = context.contentResolver.openInputStream(uri)!!
                    val fileBytes = inputStream.readBytes()
                    inputStream.close()

                    val fileName = "event_${System.currentTimeMillis()}.jpg"
                    val requestFile = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("eventPicture", fileName, requestFile)
                }

                val updated = remoteDataSource.updateEvent(
                    eventId = eventId,
                    name = name,
                    description = description,
                    startAt = startAt,
                    endAt = endAt,
                    price = price,
                    eventPicture = imagePart
                )
                _event.value = updated
                _editSuccess.value = true
            } catch (e: Exception) {
                Log.e("EditEventViewModel", "Erro ao atualizar evento: ${e.message}", e)
                _editSuccess.value = false
            }
        }
    }

    fun clearEditSuccess() {
        _editSuccess.value = false
    }
}
