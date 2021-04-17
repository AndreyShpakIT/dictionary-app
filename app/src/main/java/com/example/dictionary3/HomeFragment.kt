package com.example.dictionary3

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class HomeFragment : Fragment(), CellLongClickListener, AlertDialogClickListeners, BottomDialogOnClickListener {

    private val CODE: String = "HomeFragment"

    private lateinit var binding : FragmentHomeBinding
    private lateinit var rcAdapter : RcAdapter
    private lateinit var appContext: Context
    private lateinit var db: DbManager

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

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        db = DbManager(appContext)

        binding.buttonNewWord.setOnClickListener {
            val bottomDialog = BottomFragmentSheet(this)
            bottomDialog.show(requireFragmentManager(), "TAG")
        }

        binding.buttonResfresh.setOnClickListener() {
            Log.d(CODE, "Refresh...")
            refreshRcView()
        }

        rcAdapter = RcAdapter(ArrayList(), this, this)
        init()
        db.openDb()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        db.openDb()
        refreshRcView()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.closeDb()
    }



    // endregion

    // region Methods

    private fun init() {
        binding.rcView.layoutManager = LinearLayoutManager(appContext)
        binding.rcView.adapter = rcAdapter
    }

    private fun refreshRcView() {
        rcAdapter.updateAdapter(db.getWordList())
    }

    override fun onCellLongClickListener(word: Word) : Boolean {

        var alert = CustomAlert(appContext, this)
        alert.showDialog(word)

        return true
    }

    override fun onDeleteClickListener(word: Word) {
        Log.d(CODE, "onDeleteClickListener works...")

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

        Log.d(CODE, "onChangeClickListener works...")
    }

    override fun onBottomDialogClickListener(word: Word) {

        if (word.russianWord.isNullOrEmpty() || word.englishWord.isNullOrEmpty()) {
            Toast.makeText(appContext, "Не все поля заполнены!", Toast.LENGTH_SHORT).show()
        }
        else {
            var result : Long = db.addNewWord(word)
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

}





























