package com.griddynamics.connectedapps.ui.greeting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.griddynamics.connectedapps.MainActivity
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.model.User
import dagger.android.AndroidInjection
import dagger.android.DaggerActivity
import kotlinx.android.synthetic.main.activity_greeting.*
import javax.inject.Inject

private const val TAG: String = "GreetingActivity"
private const val RC_SIGN_IN = 191

class GreetingActivity : Activity() {

    @Inject
    lateinit var localStorage: LocalStorage
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)
        auth_sign_in_button.setOnClickListener {
            signIn()
        }
        auth_guest_sign_in_text.setOnClickListener {
            onContinue()
        }
        setupClient()
    }

    private fun setupClient() {
        val token = getString(R.string.google_sign_in_token)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            localStorage.saveUser(
                User(
                    account?.idToken,
                    account?.givenName,
                    account?.familyName,
                    account?.email
                )
            )
            Log.d(TAG, "handleSignInResult: ${account?.givenName}")
            // Signed in successfully, show authenticated UI.
            openMainScreen()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code= ${e.message} \n ${e.statusCode}", e)
        }
    }

    fun onContinue() {
        Log.d(TAG, "onContinue() called")
        openMainScreen()
    }

    private fun openMainScreen() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
}
