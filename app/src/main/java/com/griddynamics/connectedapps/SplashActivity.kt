package com.griddynamics.connectedapps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.model.EmptyUser
import com.griddynamics.connectedapps.ui.greeting.GreetingActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : Activity() {

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        doOnUserSignedIn {
            startActivity(it)
            finish()
        }

    }

    private fun doOnUserSignedIn(onSignedIn: (intent: Intent) -> Unit) {
        val intent = if (localStorage.getUser() == EmptyUser) {
            Intent(this, GreetingActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        Handler().postDelayed({
            onSignedIn(intent)
        }, 1000)
    }
}
