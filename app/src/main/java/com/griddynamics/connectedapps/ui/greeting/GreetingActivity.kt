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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
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
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)
        auth = FirebaseAuth.getInstance()
        auth_sign_in_button.setOnClickListener {
            signIn()
        }
        auth_guest_sign_in_text.setOnClickListener {
            onContinue()
        }
        setupClient()
    }

    private fun setupClient() {
        val token = getString(R.string.default_web_client_id)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
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
            firebaseAuth(account?.idToken)
            localStorage.saveUser(
                User(
                    account?.idToken,
                    account?.givenName,
                    account?.familyName,
                    account?.email,
                    account?.photoUrl.toString()
                )
            )
        } catch (e: ApiException) {
            Snackbar.make(greeting_root, "Google Authentication Failed.", Snackbar.LENGTH_SHORT)
                .show()
            Log.e(TAG, "signInResult:failed code= ${e.message}", e)
        }
    }

    private fun firebaseAuth(token: String?) {
        Log.d(TAG, "firebaseAuth() called with: token = [$token]")
        val credential = GoogleAuthProvider.getCredential(token, null)
        Log.d(TAG, "firebaseAuth: credential [${credential}]")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    auth.currentUser?.getIdToken(true)?.addOnSuccessListener {
                        Log.d(TAG, "firebaseAuth: save Firebase token [${it.token}]")
                        localStorage.saveFirebaseToken(it.token)
                    }
                    openMainScreen()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(
                        greeting_root,
                        "Firebase Authentication Failed.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
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
