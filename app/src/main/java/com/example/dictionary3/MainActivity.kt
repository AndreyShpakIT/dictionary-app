package com.example.dictionary3

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.snackbar.Snackbar
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.util.*

class MainActivity : AppCompatActivity(), com.example.dictionary3.Snackbar {

    private val _code: String = "MainActivity"
    private lateinit var bindingClass : ActivityMainBinding
    private var fragmentId: Int = 0
    private val necessaryAuth: Boolean = true
    private var driveServiceHelper: DriveServiceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        Parameters.perf = getSharedPreferences(ParametersName.P_NAME, Context.MODE_PRIVATE)
        Parameters.load()
        // region Handlers

        bindingClass.navigationBar.setOnNavigationItemSelectedListener {  item ->

            if (item.itemId == fragmentId) {
                Log.d(_code, "Navigation the same.")
                return@setOnNavigationItemSelectedListener false
            }

            when (item.itemId) {
                R.id.navigation_home -> {
                    Log.d(_code, "Navigation home works...")
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                }
                R.id.navigation_training -> {
                    Log.d(_code, "Navigation training works...")
                    val trainingFragment = TrainingFragment.newInstance()
                    openFragment(trainingFragment)
                }
                R.id.navigation_settings -> {
                    Log.d(_code, "Navigation settings works...")
                    val settingsFragment = SettingsFragment.newInstance()
                    openFragment(settingsFragment)
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            fragmentId = item.itemId
            return@setOnNavigationItemSelectedListener true
        }

        // endregion

        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }


    // region Methods

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(bindingClass.frameContainer.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // endregion
}


