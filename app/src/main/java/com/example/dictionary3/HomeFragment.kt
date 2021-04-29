package com.example.dictionary3

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.FragmentHomeBinding
import com.example.dictionary3.db.DbManager
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
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), CellListeners, AlertDialogClickListeners, BottomDialogOnClickListener, com.example.dictionary3.Snackbar {

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    private val _code: String = "HomeFragment"

    private val necessaryAuth: Boolean = true
    private var driveServiceHelper: DriveServiceHelper? = null

    private lateinit var binding : FragmentHomeBinding
    private lateinit var rcAdapter : RcAdapter
    private lateinit var appContext: Context
    private lateinit var db: DbManager

    private var edPosition = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    // region Life cycle

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // region Handlers

        binding.buttonNewWord.setOnClickListener {
            val bottomDialog = BottomFragmentSheet(this)
            bottomDialog.show(requireFragmentManager(), "TAG")
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
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

                else -> false
            }

        }



        // endregion

        openDb()

        rcAdapter = RcAdapter(ArrayList(), this)
        init()
        refreshRcView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(_code, "OnResume()...")
        //openDb()
        //refreshRcView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            500 -> {
                if (resultCode == Activity.RESULT_OK)
                    editActivityDataHandler(data)
            }
            // account intent
            400 -> {
                handleSignInIntent(data)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.home_appbar_menu, menu)
        val myActionMenuItem = menu.findItem(R.id.action_search)

        searchView = myActionMenuItem.actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                // Toast like print
                Log.d(_code, "works...: $query")
                if (!searchView!!.isIconified) {
                    searchView!!.isIconified = true
                }
                myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                Log.d(_code, "works 2...: $s")
                return false
            }
        })
        //return true
    }

    // endregion

    // region Methods

    private fun init() {
        binding.rcView.layoutManager = LinearLayoutManager(appContext)
        binding.rcView.adapter = rcAdapter
    }

    private fun refreshRcView() {

        binding.progressBar.visibility = View.VISIBLE
        db.getWordListAsync()
            .addOnSuccessListener {
                rcAdapter.updateAdapter(it)
                binding.progressBar.visibility = View.INVISIBLE
            }
            .addOnFailureListener {
                showSnackbar(binding.root,"Не удалось обратиться к базе данных")
                binding.progressBar.visibility = View.INVISIBLE
            }
    }

    override fun onCellClickListener(data: Word, pos: Int) {

        activity?.let {
            val intent = Intent(it, EditActivity::class.java)

            intent.putExtra("word", data)

            startActivityForResult(intent, 500)
        }

        edPosition = pos
    }

    override fun onDeleteClickListener(word: Word) {
        Log.d(_code, "onDeleteClickListener works...")

        val snackbar = Snackbar.make(
                binding.root,
                "Вы уверены что хотите удалить слово: ${word.englishWord}?",
                Snackbar.LENGTH_LONG
        )

        snackbar.setAction(
                "Yes"
        ) {
            db.deleteWord(word)
            refreshRcView()
        }
        snackbar.show()
    }

    override fun onChangeClickListener(word: Word) {

        Log.d(_code, "onChangeClickListener works...")
    }

    override fun onBottomDialogClickListener(word: Word) {

        if (word.russianWord.isEmpty() || word.englishWord.isEmpty()) {
            Toast.makeText(appContext, "Не все поля заполнены!", Toast.LENGTH_SHORT).show()
        }
        else {
            val result : Long = db.addNewWord(word)
            if (result > -1) {
                Toast.makeText(appContext, "Слово добавлено!", Toast.LENGTH_SHORT).show()
                refreshRcView()
            }
            else {
                if (result == (-2).toLong()) {
                    Toast.makeText(appContext, "Слово уже существует", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(appContext, "Не удалось добавить слово", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // endregion

    private fun editActivityDataHandler(data: Intent?) {

        val word = data?.extras?.get("res") as Word
        showSnackbar(binding.root, "Слово изменено: ${word.russianWord} | ${word.englishWord} (${word.id})")

        db.updateWord(word)

        rcAdapter.updateItem(word, edPosition)
    }
    private fun openDb() {
        db = DbManager(appContext)
        db.openDb()
    }
    private fun closeDb() {
        db.closeDb()
    }

    // region Google Drive

    private fun signIn() {

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()

        val client: GoogleSignInClient = GoogleSignIn.getClient(appContext, signInOptions)
        client.signOut()
        startActivityForResult(client.signInIntent, 400)
    }

    private fun handleSignInIntent(data: Intent?) {

        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener {

                    val credential = GoogleAccountCredential
                            .usingOAuth2(appContext, Collections.singleton(DriveScopes.DRIVE_FILE))

                    credential.selectedAccount = it.account

                    val googleDriveService = Drive.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            GsonFactory(),
                            credential)
                            .setApplicationName("Android Dictionary")
                            .build()

                    driveServiceHelper = DriveServiceHelper(googleDriveService)
                    showSnackbar(binding.root, "Авторизация выполнена")
                }
                .addOnFailureListener {
                    showSnackbar(binding.root, "Авторизация не выполнена")
                }

    }

    private fun uploadFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(binding.root, "Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(appContext)
        progressDialog.setTitle("Отправка")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper?.createFileAsync(Version.getNowVersion().value)
                ?.addOnSuccessListener {
                    progressDialog.dismiss()

                    showSnackbar(binding.root, "Файл успешно загружен")
                }
                ?.addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(binding.root, it.message ?: "Пустое сообщение")
                }

    }

    private fun getFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(binding.root, "Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(appContext)
        progressDialog.setTitle("Получение файла")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper!!.getFileAsync()
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    showSnackbar(binding.root, "Файл успешно получен. Id: ${it.id}")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(binding.root, it.message ?: "Пустое сообщение")
                }
    }

    private fun updateFile() {

        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(binding.root, "Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(appContext)
        progressDialog.setTitle("Обновление файла")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper!!.updateFileAsync()
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    showSnackbar(binding.root, "Файл успешно обновлен. Id: ${it.id}")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(binding.root, it.message ?: "Пустое сообщение")
                }
    }

    private fun downloadFile() {
        if (driveServiceHelper == null) {

            if (necessaryAuth) {
                showSnackbar(binding.root, "Необходимо авторизоваться")
                return
            }

            signIn()

        }

        val progressDialog = ProgressDialog(appContext)
        progressDialog.setTitle("Скачивание файла")
        progressDialog.setMessage("Пожалуйста подождите...")
        progressDialog.show()

        driveServiceHelper!!.downloadFileAsync()
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    showSnackbar(binding.root, "Файл успешно скачан.")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    showSnackbar(binding.root, it.message ?: "Пустое сообщение")
                }
    }

    // endregion
}




















