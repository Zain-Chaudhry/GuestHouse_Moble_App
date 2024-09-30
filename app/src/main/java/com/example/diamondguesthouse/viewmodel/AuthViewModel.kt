package com.example.diamondguesthouse.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class AuthViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState


    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.UnAuthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {

        if(email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    _authState.value = AuthState.Authenticated

                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")


                }
            }

    }

    fun signUp(name: String, email: String, password: String) {

        if(email.isEmpty() || password.isEmpty()|| name.isEmpty()) {
            _authState.value = AuthState.Error("Name, Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    _authState.value = AuthState.Authenticated

                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")


                }
            }

    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("Email cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success("Password reset email sent successfully.")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error sending password reset email")
                }
            }
    }

    fun handleGoogleSignIn(context: Context) {

        viewModelScope.launch {
            signInWithGoogle(context).collect{ result ->
                result.onSuccess {
                    _authState.value = AuthState.Authenticated
                }
                result.onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Something went wrong")
                }
            }

        }
    }

    private suspend fun signInWithGoogle(context: Context): Flow<Result<AuthResult>> {

        return callbackFlow {

            try {

                // credential manager initialized
                val credentialManager: CredentialManager = CredentialManager.create(context)

                // Nonce generation
                val ranNonce: String = UUID.randomUUID().toString()
                val bytes: ByteArray = ranNonce.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                val digest: ByteArray = md.digest(bytes)
                val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.Web_Client_ID))
                    .setNonce(hashedNonce)
                    .setAutoSelectEnabled(true)
                    .build()


                val request: androidx.credentials.GetCredentialRequest =
                    androidx.credentials.GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    val authResult = auth.signInWithCredential(authCredential).await()

                    trySend(Result.success(authResult))

                } else {
                    throw RuntimeException("Received invalid credential type")
                }
            } catch (e: GetCredentialCancellationException) {
                trySend(Result.failure(Exception("User cancelled the sign-in process")))
            }catch (e: Exception) {
                trySend(Result.failure(e))
        }
            awaitClose { }
        }
    }
}


sealed class AuthState{
    data object Authenticated: AuthState()
    data object UnAuthenticated: AuthState()
    data object Loading: AuthState()
    data class Error(val message: String): AuthState()
    data class Success(val message: String): AuthState()

}