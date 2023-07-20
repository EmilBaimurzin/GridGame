package com.grid.game.ui.settings

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.grid.game.R
import com.grid.game.core.library.ViewBindingDialog
import com.grid.game.databinding.DialogSettingsBinding
import com.grid.game.ui.game.CallbackViewModel
import com.grid.game.ui.other.MainActivity

class DialogSettings : ViewBindingDialog<DialogSettingsBinding>(DialogSettingsBinding::inflate) {
    private val sharedPrefs by lazy {
        requireActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
    }
    private val viewModel: CallbackViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack()
                viewModel.callback?.invoke()
                true
            } else {
                false
            }
        }
        setImage()

        binding.volumeButton.setOnClickListener {
            changeVolumeState()
        }

        binding.continueButton.setOnClickListener {
            findNavController().popBackStack()
            viewModel.callback?.invoke()
        }
    }

    private fun changeVolumeState() {
        val volumeState = sharedPrefs.getBoolean("VOLUME", true)
        sharedPrefs.edit().putBoolean("VOLUME",!volumeState).apply()
        if (sharedPrefs.getBoolean("VOLUME", true)) {
            (requireActivity() as MainActivity).startMusic()
        } else {
            (requireActivity() as MainActivity).pauseMusic()
        }
        setImage()
    }

    private fun setImage() {
        val volumeState = sharedPrefs.getBoolean("VOLUME", true)
        val img = if (volumeState) {
            R.drawable.button_sound_on
        } else {
            R.drawable.button_sound_off
        }
        binding.volumeButton.setImageResource(img)
    }
}