package com.griddynamics.connectedapps

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.griddynamics.connectedapps.gateway.local.LocalStorage
import com.griddynamics.connectedapps.gateway.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.model.EmptyUser
import com.griddynamics.connectedapps.service.ScannerDataUpdateService
import com.griddynamics.connectedapps.ui.greeting.GreetingActivity
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        ScannerDataUpdateService.startActionStart(applicationContext)
    }

    fun logout() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut()
        localStorage.saveUser(EmptyUser)
        val intent = Intent(this, GreetingActivity::class.java)
        startActivity(intent)
        finish()
    }
}
