package com.austral.nutri_planner_ts.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.austral.nutri_planner_ts.security.BiometricAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val biometricAuthManager: BiometricAuthManager,
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun authenticate(context: Context) {
        biometricAuthManager.authenticate(
            context = context,
            onError = { _isAuthenticated.value = false },
            onSuccess = { _isAuthenticated.value = true },
            onFail = { _isAuthenticated.value = false }
        )
    }
} 