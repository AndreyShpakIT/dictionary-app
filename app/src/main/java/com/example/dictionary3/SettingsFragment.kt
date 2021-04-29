package com.example.dictionary3

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dictionary3.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    private lateinit var rcAdapter : RcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        initializeHandlers()

        return binding.root
    }

    private fun initializeHandlers() {

        binding.buttonEditLWords.setOnClickListener {

            val intent = Intent(activity, LearningListActivity::class.java)
            startActivity(intent)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked)
                activity?.setTheme(R.style.Theme_Black)
            else
                activity?.setTheme(R.style.Theme_Light)

        }

    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

}