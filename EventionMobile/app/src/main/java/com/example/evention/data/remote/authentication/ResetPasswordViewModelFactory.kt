package com.example.evention.data.remote.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.evention.di.NetworkModule
import com.example.evention.ui.screens.auth.resetpassword.ResetPasswordViewModel

class ResetPasswordViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val resetPasswordRemoteDataSource = NetworkModule.resetPasswordRemoteDataSource
        return ResetPasswordViewModel(resetPasswordRemoteDataSource) as T
    }
}

