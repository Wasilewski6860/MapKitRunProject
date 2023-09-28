package com.example.mapkitproject.presentation.setup_fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.mapkitproject.R
import com.example.mapkitproject.databinding.ActivityMainBinding
import com.example.mapkitproject.databinding.FragmentSetupBinding
import com.example.mapkitproject.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.mapkitproject.other.Constants.KEY_IS_FIRST
import com.example.mapkitproject.other.Constants.KEY_NAME
import com.example.mapkitproject.other.Constants.KEY_WEIGHT
import com.example.mapkitproject.other.Constants.SHARED_PREFERENCES_NAME
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class SetupFragment : Fragment(R.layout.fragment_setup) {


    private val sharedPreferences: SharedPreferences by inject()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject()

    var isFirstAppOpen = true

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    private var _mainBinding: ActivityMainBinding? = null
    private val mainBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirstAppOpen = sharedPreferences.getBoolean(KEY_IS_FIRST,false)
        if(!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        binding.tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if(success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun writePersonalDataToSharedPref(): Boolean {

        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPreferences.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .putBoolean(KEY_IS_FIRST,true)
            .apply()
        val toolbarText = "Let's go, $name!"
        requireActivity().findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText
        return true
    }


}