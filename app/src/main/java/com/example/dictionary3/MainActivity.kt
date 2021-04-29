package com.example.dictionary3

import android.app.ProgressDialog
import android.content.Intent
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

    // region Life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

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

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            // account intent
            400 -> {
                handleSignInIntent(data)
            }
        }

    }*/

    // endregion



    // region Methods

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(bindingClass.frameContainer.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /*private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
                bindingClass.root,
                message,
                Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }*/

    // endregion

    // region Google Drive
/*
    private fun signIn() {

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()

        val client: GoogleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        client.signOut()
        startActivityForResult(client.signInIntent, 400)
    }

    private fun handleSignInIntent(data: Intent?) {

        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener {

                    val credential = GoogleAccountCredential
                            .usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE))

                    credential.selectedAccount = it.account

                    val googleDriveService = Drive.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            GsonFactory(),
                            credential)
                            .setApplicationName("Android Dictionary")
                            .build()

                    driveServiceHelper = DriveServiceHelper(googleDriveService)
                    showSnackbar(bindingClass.root,"Авторизация выполнена")
                }
                .addOnFailureListener {
                    showSnackbar(bindingClass.root,"Авторизация не выполнена")
                }

    }

    private fun uploadFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(bindingClass.root,"Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Отправка")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper?.createFileAsync(Version.getNowVersion().value)
                ?.addOnSuccessListener {
                    progressDialog.dismiss()

                    showSnackbar(bindingClass.root,"Файл успешно загружен")
                }
                ?.addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(bindingClass.root,it.message ?: "Пустое сообщение")
                }

    }

    private fun getFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(bindingClass.root,"Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Получение файла")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper!!.getFileAsync()
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    showSnackbar(bindingClass.root,"Файл успешно получен. Id: ${it.id}")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(bindingClass.root,it.message ?: "Пустое сообщение")
                }
    }

    private fun updateFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(bindingClass.root,"Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Обновление файла")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper!!.updateFileAsync()
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    showSnackbar(bindingClass.root,"Файл успешно обновлен. Id: ${it.id}")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(bindingClass.root,it.message ?: "Пустое сообщение")
                }
    }

    private fun downloadFile() {
        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(bindingClass.root,"Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Скачивание файла")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper!!.downloadFileAsync()
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    showSnackbar(bindingClass.root,"Файл успешно скачан.")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(bindingClass.root,it.message ?: "Пустое сообщение")
                }
    }*/

    // endregion

}


