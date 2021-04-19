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

class MainActivity : AppCompatActivity() {

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
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            fragmentId = item.itemId
            return@setOnNavigationItemSelectedListener true
        }

        bindingClass.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.actionbar_button_upload -> {

                    updateFile()

                    Log.d(_code, "actionbar_button_upload works...")
                    true
                }

                R.id.actionbar_button_download -> {

                    downloadFile()

                    Log.d(_code, "actionbar_button_download works...")
                    true
                }

                R.id.actionbar_button_check -> {

                    Log.d(_code, "actionbar_button_check works...")
                    true
                }

                R.id.actionbar_button_change -> {

                    signIn()

                    Log.d(_code, "actionbar_button_change works...")
                    true
                }

                R.id.actionbar_button_search ->{
                    Log.d(_code, "actionbar_button_search works...")
                    true
                }

                else -> false
            }

        }

    // endregion
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            // account intent
            400 -> {
                handleSignInIntent(data)
                /*if (resultCode == RESULT_OK) {

                }
                else {
                    showSnackbar("Не удалось выполнить вход в Google account")
                }*/
            }

        }

    }

    // endregion



    // region Methods

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(bindingClass.frameContainer.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
                bindingClass.root,
                message,
                Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    // endregion

    // region Google Drive

    private fun signIn() {

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()

        val client: GoogleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        //client.signOut()
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
                    showSnackbar("Авторизация выполнена")
                }
                .addOnFailureListener {
                    showSnackbar("Авторизация не выполнена")
                }

    }

    private fun uploadFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar("Необходимо авторизоваться")
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

                    showSnackbar("Файл успешно загружен")
                }
                ?.addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(it.message ?: "Пустое сообщение")
                }

    }

    private fun getFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar("Необходимо авторизоваться")
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
                    showSnackbar("Файл успешно получен. Id: ${it.id}")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(it.message ?: "Пустое сообщение")
                }
    }

    private fun updateFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar("Необходимо авторизоваться")
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
                    showSnackbar("Файл успешно обновлен. Id: ${it.id}")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(it.message ?: "Пустое сообщение")
                }
    }

    private fun downloadFile() {
        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar("Необходимо авторизоваться")
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
                    showSnackbar("Файл успешно скачан.")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(it.message ?: "Пустое сообщение")
                }
    }

    // endregion

}


    // region Interfaces
    interface CellLongClickListener {
        fun onCellLongClickListener(data: Word) : Boolean
    }

    interface AlertDialogClickListeners {
        fun onDeleteClickListener(word: Word)
        fun onChangeClickListener(word: Word)
    }

    interface BottomDialogOnClickListener {
        fun onBottomDialogClickListener(word: Word)
    }
    // endregion