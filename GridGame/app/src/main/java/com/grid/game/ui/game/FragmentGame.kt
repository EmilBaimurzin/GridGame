package com.grid.game.ui.game

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.grid.game.R
import com.grid.game.databinding.FragmentGameBinding
import com.grid.game.domain.game.adapter.GameAdapter
import com.grid.game.ui.other.ViewBindingFragment


class FragmentGame : ViewBindingFragment<FragmentGameBinding>(FragmentGameBinding::inflate) {
    private lateinit var gameAdapter: GameAdapter
    private val viewModel: GameViewModel by viewModels()
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        callbackViewModel.callback = {
            viewModel.startTimer()
            viewModel.pauseState = false
        }

        binding.menuButton.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }

        binding.restartButton.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentGame)
        }

        binding.settingsButton.setOnClickListener {
            viewModel.stopTimer()
            viewModel.pauseState = true
            findNavController().navigate(R.id.action_fragmentGame_to_dialogSettings)
        }

        binding.pauseButton.setOnClickListener {
            viewModel.stopTimer()
            viewModel.pauseState = true
            findNavController().navigate(R.id.action_fragmentGame_to_dialogSettings)
        }

        viewModel.list.observe(viewLifecycleOwner) {
            gameAdapter.items = it.toMutableList()
            gameAdapter.notifyDataSetChanged()
        }
        viewModel.lives.observe(viewLifecycleOwner) {
            binding.heart1.isVisible = it >= 1
            binding.heart2.isVisible = it >= 2
            binding.heart3.isVisible = it >= 3
            if (it == 0 && viewModel.gameState) {
                endGame()
            }
        }
        viewModel.time.observe(viewLifecycleOwner) {
            binding.timer.text = it.toString()

            if (it == 0 && viewModel.gameState) {
                endGame()
            }
        }

        viewModel.scores.observe(viewLifecycleOwner) {
            binding.scores.text = it.toString()
        }

        if (viewModel.gameState && !viewModel.pauseState) {
            viewModel.startTimer()
        }
    }

    private fun endGame() {
        viewModel.gameState = false
        viewModel.stopTimer()
        findNavController().navigate(FragmentGameDirections.actionFragmentGameToDialogGameOver(viewModel.scores.value!!))
    }

    private fun initAdapter() {
        gameAdapter = GameAdapter {
            viewModel.itemClick(it)
        }
        with(binding.gameRV) {
            adapter = gameAdapter
            layoutManager = GridLayoutManager(requireContext(), 4).apply {
                orientation = GridLayoutManager.VERTICAL
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }
}