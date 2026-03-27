package com.example.diamondguesthouse.domain.repo

import android.content.Context
import com.example.diamondguesthouse.domain.models.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>

    fun login(email: String, password: String)
    fun signUp(name: String, email: String, password: String)
    fun signOut()
    fun resetPassword(email: String)
    fun handleGoogleSignIn(context: Context)
}
