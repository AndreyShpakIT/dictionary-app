package com.example.dictionary3

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dictionary3.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    private lateinit var rcAdapter : RcAdapter

    companion object {
        fun newInstance() = SettingsFragment()
    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        Parameters.load()

        initDefault()
        initHandlers()
        return binding.root

    }

    private fun initDefault() {
        binding.radioButtonWords.isChecked = Parameters.genType == 0
        if (Parameters.genType == 0) {
            binding.buttonEditLWords.visibility = View.GONE
            binding.dictPanel.visibility = View.VISIBLE
        }
        else {
            binding.buttonEditLWords.visibility = View.VISIBLE
            binding.dictPanel.visibility = View.GONE
        }
        binding.radioButtonLWords.isChecked = Parameters.genType != 0
        binding.switchGreen.isChecked = Parameters.dictionaryFilterGreen
        binding.switchOrange.isChecked = Parameters.dictionaryFilterOrange
        binding.switchRed.isChecked = Parameters.dictionaryFilterRed
        binding.switchEnglish.isChecked = Parameters.languageEnglish
        binding.switchRussian.isChecked = Parameters.languageRussian
        binding.switchTheme.isChecked = Parameters.darkTheme
    }
    private fun initHandlers() {

        binding.buttonEditLWords.setOnClickListener {
            val intent = Intent(activity, LearningListActivity::class.java)
            startActivity(intent)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            Parameters.darkTheme = isChecked
            Parameters.save()
        }

        binding.switchEnglish.setOnCheckedChangeListener { _, isChecked ->
            Parameters.languageEnglish = isChecked
            Parameters.save()
        }

        binding.switchRussian.setOnCheckedChangeListener { _, isChecked ->
            Parameters.languageRussian = isChecked
            Parameters.save()
        }

        binding.switchGreen.setOnCheckedChangeListener { _, isChecked ->
            Parameters.dictionaryFilterGreen = isChecked
            Parameters.save()
        }

        binding.switchOrange.setOnCheckedChangeListener { _, isChecked ->
            Parameters.dictionaryFilterOrange = isChecked
            Parameters.save()
        }

        binding.switchRed.setOnCheckedChangeListener { _, isChecked ->
            Parameters.dictionaryFilterRed = isChecked
            Parameters.save()
        }

        binding.switchEnglish.setOnCheckedChangeListener { _, isChecked ->
            Parameters.languageEnglish = isChecked
            Parameters.save()
        }

        binding.radioButtonWords.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.dictPanel.visibility = View.VISIBLE
                binding.buttonEditLWords.visibility = View.GONE
            } else {
                binding.dictPanel.visibility = View.GONE
                binding.buttonEditLWords.visibility = View.VISIBLE
            }
            Parameters.genType = if (isChecked) 0 else 1
            Parameters.save()
        }
        binding.radioButtonLWords.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.dictPanel.visibility = View.GONE
                binding.buttonEditLWords.visibility = View.VISIBLE
            } else {
                binding.dictPanel.visibility = View.VISIBLE
                binding.buttonEditLWords.visibility = View.GONE
            }
            Parameters.genType = if (isChecked) 1 else 0
            Parameters.save()
        }

        binding.imbDone.setOnClickListener {
            Parameters.genType = if (binding.radioButtonLWords.isChecked) 0 else 1
            Parameters.dictionaryFilterGreen = binding.switchGreen.isChecked
            Parameters.dictionaryFilterOrange = binding.switchOrange.isChecked
            Parameters.dictionaryFilterRed = binding.switchRed.isChecked
            Parameters.languageEnglish = binding.switchEnglish.isChecked
            Parameters.languageRussian = binding.switchRussian.isChecked
            Parameters.darkTheme = binding.switchTheme.isChecked
            Parameters.save()
            Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()
        }

    }
}