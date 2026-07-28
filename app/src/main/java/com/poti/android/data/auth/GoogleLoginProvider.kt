package com.poti.android.data.auth

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.poti.android.BuildConfig
import com.poti.android.core.auth.SocialLoginResult
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class GoogleLoginProvider @Inject constructor() {
    suspend fun login(context: Context): SocialLoginResult =
        try {
            val response = CredentialManager.create(context).getCredential(
                context = context,
                request = createCredentialRequest(),
            )

            response.credential.toSocialLoginResult()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (_: GetCredentialCancellationException) {
            SocialLoginResult.Cancelled
        } catch (error: Exception) {
            SocialLoginResult.Failure(error)
        }

    private fun createCredentialRequest(): GetCredentialRequest {
        val googleSignInOption = GetSignInWithGoogleOption.Builder(
            serverClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID,
        ).build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleSignInOption)
            .build()
    }

    private fun Credential.toSocialLoginResult(): SocialLoginResult {
        if (
            this !is CustomCredential ||
            type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return SocialLoginResult.Failure(
                IllegalStateException("Unsupported Google credential type: $type"),
            )
        }

        val googleCredential = GoogleIdTokenCredential.createFrom(data)
        return SocialLoginResult.Success(token = googleCredential.idToken)
    }
}
