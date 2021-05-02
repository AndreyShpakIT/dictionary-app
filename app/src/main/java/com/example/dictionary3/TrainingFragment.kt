package com.example.dictionary3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dictionary3.databinding.FragmentHomeBinding
import com.example.dictionary3.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private lateinit var binding: FragmentTrainingBinding

    companion object {
        fun newInstance() = TrainingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTrainingBinding.inflate(inflater, container, false)

        initHandlers()

        return binding.root
    }


    private fun initHandlers() {

        binding.cardVocabulary.setOnClickListener {
            val intent = Intent(context, TrainingActivity::class.java)
            startActivity(intent)
        }

    }
}


















