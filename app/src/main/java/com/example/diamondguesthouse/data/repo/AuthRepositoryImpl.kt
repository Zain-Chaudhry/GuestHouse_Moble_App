package com.example.diamondguesthouse.data.repo

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.domain.models.AuthState
import com.example.diamondguesthouse.domain.repo.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single
import java.security.MessageDigest
import java.util.UUID
@Single(binds = [AuthRepository::class])
class AuthRepositoryImpl : AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _authState = MutableStateFlow<AuthState>(AuthState.UnAuthenticated)
    override val authState = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        _authState.value = if (auth.currentUser == null) {
            AuthState.UnAuthenticated
        } else {
            AuthState.Authenticated
        }
    }

    override fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.Authenticated
                } else {
                    AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    override fun signUp(name: String, email: String, password: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthState.Error("Name, Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.Authenticated
                } else {
                    AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    override fun signOut() {
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

    override fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("Email cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.Success("Password reset email sent successfully.")
                } else {
                    AuthState.Error(task.exception?.message ?: "Error sending password reset email")
                }
            }
    }

    override fun handleGoogleSignIn(context: Context) {
        scope.launch {
            signInWithGoogle(context).collect { result ->
                result.onSuccess {
                    _authState.value = AuthState.Authenticated
                }
                result.onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Something went wrong")
                }
            }
        }
    }

    private fun signInWithGoogle(context: Context): Flow<Result<AuthResult>> = flow {
        try {
            val credentialManager = CredentialManager.create(context)
            val ranNonce: String = UUID.randomUUID().toString()
            val digest: ByteArray = MessageDigest.getInstance("SHA-256").digest(ranNonce.toByteArray())
            val hashedNonce: String = digest.fold("") { str, b -> str + "%02x".format(b) }

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.Web_Client_ID))
                .setNonce(hashedNonce)
                .setAutoSelectEnabled(true)
                .build()

            val request = androidx.credentials.GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credResult = credentialManager.getCredential(context, request)
            val credential = credResult.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = auth.signInWithCredential(authCredential).await()
                emit(Result.success(authResult))
            } else {
                emit(Result.failure(RuntimeException("Received invalid credential type")))
            }
        } catch (e: GetCredentialCancellationException) {
            emit(Result.failure(Exception("User cancelled the sign-in process")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
