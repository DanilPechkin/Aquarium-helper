package com.danilp.aquariumhelper.domain.service.impl

import com.danilp.aquariumhelper.domain.service.AccountService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class AccountServiceImpl @Inject constructor() : AccountService {
    override fun hasUser(): Boolean =
        Firebase.auth.currentUser != null

    override fun isAnonymousUser(): Boolean =
        Firebase.auth.currentUser?.isAnonymous ?: true

    override fun getUserEmail(): String =
        Firebase.auth.currentUser?.email.orEmpty()

    override fun getUserId(): String =
        Firebase.auth.currentUser?.uid.orEmpty()

    override fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun sendRecoveryEmail(email: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun createAnonymousAccount(onResult: (Throwable?) -> Unit) {
        Firebase.auth.signInAnonymously()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        val credential = EmailAuthProvider.getCredential(email, password)

        Firebase.auth.currentUser?.linkWithCredential(credential)
            ?.addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteAccount(onResult: (Throwable?) -> Unit) {
        Firebase.auth.currentUser?.delete()
            ?.addOnCompleteListener { onResult(it.exception) }
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}