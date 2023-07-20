package com.grid.game.ui.main

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.grid.game.R
import com.grid.game.databinding.FragmentMainBinding
import com.grid.game.ui.other.ViewBindingFragment

class FragmentMain: ViewBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentGame)
        }
        binding.buttonExit.setOnClickListener {
            requireActivity().finish()
        }
        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(Intent(ACTION_VIEW, Uri.parse("https://www.google.com")))
        }
    }
}