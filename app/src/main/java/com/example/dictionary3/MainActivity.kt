package com.example.dictionary3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.ActivityMainBinding
import com.example.dictionary3.db.DbManager
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private val CODE: String = "MainActivity"
    private lateinit var bindingClass : ActivityMainBinding
    private var fragmentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        bindingClass.navigationBar.setOnNavigationItemSelectedListener {  item ->

            if (item.itemId == fragmentId) {
                Log.d(CODE, "Navigation the same.")
                return@setOnNavigationItemSelectedListener false
            }

            when (item.itemId) {
                R.id.navigation_home -> {
                    Log.d(CODE, "Navigation home works...")
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                }
                R.id.navigation_training -> {
                    Log.d(CODE, "Navigation training works...")
                    val trainingFragment = TrainingFragment.newInstance()
                    openFragment(trainingFragment)
                }
                R.id.navigation_settings -> {
                    Log.d(CODE, "Navigation settings works...")
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            fragmentId = item.itemId
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(bindingClass.frameContainer.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}

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