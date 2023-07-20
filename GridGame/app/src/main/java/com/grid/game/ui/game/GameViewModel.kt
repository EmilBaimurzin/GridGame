package com.grid.game.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grid.game.core.library.l
import com.grid.game.domain.game.GameRepository
import com.grid.game.domain.game.adapter.GameItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val repository = GameRepository()
    var gameState = true
    var pauseState = false
    private var gameScope = CoroutineScope(Dispatchers.Default)
    private val _list = MutableLiveData(repository.generateList())
    val list: LiveData<List<GameItem>> = _list

    private val _lives = MutableLiveData(3)
    val lives: LiveData<Int> = _lives

    private val _scores = MutableLiveData(0)
    val scores: LiveData<Int> = _scores

    private val _time = MutableLiveData(60)
    val time: LiveData<Int> = _time

    private fun addScores(isBig: Boolean) {
        val value = if (isBig) 20 else 12
        _scores.postValue(_scores.value!! + value)
    }

    private fun removeLives() {
        _lives.postValue(_lives.value!! - 1)
    }

    fun startTimer() {
        gameScope = CoroutineScope(Dispatchers.Default)
        gameScope.launch {
            var value = _time.value!!
            while (value != 0) {
                _time.postValue(value - 1)
                value -= 1
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        gameScope.cancel()
    }

    fun itemClick(position: Int) {
        val currentList = _list.value!!
        val findItem = currentList.find { it.isSelected }
        if (findItem == null) {
            currentList[position].isSelected = true
            _list.postValue(currentList)
        } else {
            if (currentList[position].isSelected) {
                currentList[position].isSelected = false
                _list.postValue(currentList)
            } else {
                val selectedItem = currentList.find { it.isSelected }
                val selectedItemPosition = currentList.indexOf(selectedItem)
                checkDirection(selectedItemPosition, position)
            }
        }
    }

    private fun checkDirection(selectedItem: Int, clickedItem: Int) {
        if (clickedItem == selectedItem - 4) {
            move(selectedItem, clickedItem)
        }
        if (clickedItem == selectedItem - 1 && clickedItem != 3 && clickedItem != 7 && clickedItem != 11 && clickedItem != 15) {
            move(selectedItem, clickedItem)
        }
        if (clickedItem == selectedItem + 1 && clickedItem != 0 && clickedItem != 4 && clickedItem != 8 && clickedItem != 12 && clickedItem != 16) {
            move(selectedItem, clickedItem)
        }
        if (clickedItem == selectedItem + 4) {
            move(selectedItem, clickedItem)
        }
    }

    private fun move(selectedItem: Int, clickedItem: Int) {
        viewModelScope.launch {
            val currentList = _list.value!!.toMutableList()
            val currentSelectedItem = currentList[selectedItem]
            currentSelectedItem.isSelected = false
            val currentClickedItem = currentList[clickedItem]
            currentClickedItem.isSelected = false
            currentList[selectedItem] = currentClickedItem
            currentList[clickedItem] = currentSelectedItem
            _list.postValue(currentList)
            delay(20)
            checkRow(_list.value!!.indexOf(currentSelectedItem))
        }
    }

    private fun checkRow(index: Int): Boolean {
        l(index.toString())
        if (index in (0..3)) {
            val firstRow = checkVerticalRowFirst(index)
            val checkHorizontalRow1 = checkHorizontalRow(0)
            val checkHorizontalRow2 = checkHorizontalRow(1)
            if (firstRow) {
                removeVerticalRow(index)
                addScores(false)
                return true
            }
            if (checkHorizontalRow1 && checkHorizontalRow2) {
                removeFullRow(0)
                addScores(true)
                return true
            }
            if (checkHorizontalRow1) {
                removeHorizontalRow(0)
                addScores(false)
                return true
            }
            if (checkHorizontalRow2) {
                addScores(false)
                removeHorizontalRow(1)
                return true
            }
        }
        if (index in (4..7)) {
            val firstRow = checkVerticalRowFirst(index)
            val secondRow = checkVerticalRowSecond(index)
            val checkHorizontalRow1 = checkHorizontalRow(4)
            val checkHorizontalRow2 = checkHorizontalRow(5)
            if (firstRow) {
                removeVerticalRow(index)
                addScores(false)
                return true
            }
            if (secondRow) {
                removeVerticalRow(index - 4)
                addScores(false)
                return true
            }

            if (checkHorizontalRow1 && checkHorizontalRow2) {
                removeFullRow(4)
                addScores(true)
                return true
            }
            if (checkHorizontalRow1) {
                removeHorizontalRow(4)
                addScores(false)
                return true
            }
            if (checkHorizontalRow2) {
                removeHorizontalRow(5)
                addScores(false)
                return true
            }
        }

        if (index in (8..11)) {
            val firstRow = checkVerticalRowFirst(index)
            val secondRow = checkVerticalRowSecond(index)
            val thirdRow = checkVerticalRowThird(index)
            val checkHorizontalRow1 = checkHorizontalRow(8)
            val checkHorizontalRow2 = checkHorizontalRow(9)
            if (firstRow) {
                removeVerticalRow(index)
                addScores(false)
                return true
            }
            if (secondRow) {
                removeVerticalRow(index - 4)
                addScores(false)
                return true
            }
            if (thirdRow) {
                removeVerticalRow(index - 8)
                addScores(false)
                return true
            }

            if (checkHorizontalRow1 && checkHorizontalRow2) {
                removeFullRow(8)
                addScores(true)
                return true
            }
            if (checkHorizontalRow1) {
                removeHorizontalRow(8)
                addScores(false)
                return true
            }
            if (checkHorizontalRow2) {
                removeHorizontalRow(9)
                addScores(false)
                return true
            }
        }
        if (index in (12..15)) {
            val secondRow = checkVerticalRowSecond(index)
            val thirdRow = checkVerticalRowThird(index)
            val checkHorizontalRow1 = checkHorizontalRow(12)
            val checkHorizontalRow2 = checkHorizontalRow(13)
            if (secondRow) {
                removeVerticalRow(index - 4)
                addScores(false)
                return true
            }
            if (thirdRow) {
                removeVerticalRow(index - 8)
                addScores(false)
                return true
            }
            if (checkHorizontalRow1 && checkHorizontalRow2) {
                removeFullRow(12)
                addScores(true)
                return true
            }
            if (checkHorizontalRow1) {
                removeHorizontalRow(12)
                addScores(false)
                return true
            }
            if (checkHorizontalRow2) {
                removeHorizontalRow(13)
                addScores(false)
                return true
            }
        }
        if (index in (16..19)) {
            val thirdRow = checkVerticalRowThird(index)
            val checkHorizontalRow1 = checkHorizontalRow(16)
            val checkHorizontalRow2 = checkHorizontalRow(17)
            if (thirdRow) {
                removeVerticalRow(index - 8)
                addScores(false)
                return true
            }
            if (checkHorizontalRow1 && checkHorizontalRow2) {
                removeFullRow(16)
                return true
            }
            if (checkHorizontalRow1) {
                removeHorizontalRow(16)
                addScores(false)
                return true
            }
            if (checkHorizontalRow2) {
                removeHorizontalRow(17)
                addScores(false)
                return true
            }
        }
        removeLives()
        return false
    }

    private fun removeVerticalRow(index: Int) {
        val currentList = _list.value!!.toMutableList()
        when (index) {
            in (0..3) -> {
                currentList[index] = repository.getRandomItem()
                currentList[index + 4] = repository.getRandomItem()
                currentList[index + 8] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (4..7) -> {
                currentList[index + 8] = currentList[index - 4]

                currentList[index - 4] = repository.getRandomItem()
                currentList[index] = repository.getRandomItem()
                currentList[index + 4] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (8..11) -> {
                currentList[index + 8] = currentList[index - 4]
                currentList[index + 4] = currentList[index - 8]

                currentList[index - 8] = repository.getRandomItem()
                currentList[index - 4] = repository.getRandomItem()
                currentList[index] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (12..15) -> {
                currentList[index + 8] = currentList[index - 4]
                currentList[index + 4] = currentList[index - 8]

                currentList[index - 8] = repository.getRandomItem()
                currentList[index - 4] = repository.getRandomItem()
                currentList[index] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (16..19) -> {
                currentList[index + 8] = currentList[index - 4]
                currentList[index + 4] = currentList[index - 8]

                currentList[index - 8] = repository.getRandomItem()
                currentList[index - 4] = repository.getRandomItem()
                currentList[index] = repository.getRandomItem()
                _list.postValue(currentList)
            }
        }
    }

    private fun removeFullRow(index: Int) {
        val currentList = _list.value!!.toMutableList()
        when (index) {
            in (0..3) -> {
                currentList[index] = repository.getRandomItem()
                currentList[index + 1] = repository.getRandomItem()
                currentList[index + 2] = repository.getRandomItem()
                currentList[index + 3] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (4..7) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]
                currentList[index + 3] = currentList[index - 4 + 3]

                currentList[index - 4] = repository.getRandomItem()
                currentList[index - 4 + 1] = repository.getRandomItem()
                currentList[index - 4 + 2] = repository.getRandomItem()
                currentList[index - 4 + 3] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (8..11) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]
                currentList[index + 3] = currentList[index - 4 + 3]

                currentList[index - 4] = currentList[index - 8]
                currentList[index - 4 + 1] = currentList[index - 8 + 1]
                currentList[index - 4 + 2] = currentList[index - 8 + 2]
                currentList[index - 4 + 3] = currentList[index - 8 + 3]

                currentList[index - 8] = repository.getRandomItem()
                currentList[index - 8 + 1] = repository.getRandomItem()
                currentList[index - 8 + 2] = repository.getRandomItem()
                currentList[index - 8 + 3] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (12..15) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]
                currentList[index + 3] = currentList[index - 4 + 3]

                currentList[index - 4] = currentList[index - 8]
                currentList[index - 4 + 1] = currentList[index - 8 + 1]
                currentList[index - 4 + 2] = currentList[index - 8 + 2]
                currentList[index - 4 + 3] = currentList[index - 8 + 3]

                currentList[index - 8] = currentList[index - 12]
                currentList[index - 8 + 1] = currentList[index - 12 + 1]
                currentList[index - 8 + 2] = currentList[index - 12 + 2]
                currentList[index - 8 + 3] = currentList[index - 12 + 3]

                currentList[index - 12] = repository.getRandomItem()
                currentList[index - 12 + 1] = repository.getRandomItem()
                currentList[index - 12 + 2] = repository.getRandomItem()
                currentList[index - 12 + 3] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (16..19) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]
                currentList[index + 3] = currentList[index - 4 + 3]

                currentList[index - 4] = currentList[index - 8]
                currentList[index - 4 + 1] = currentList[index - 8 + 1]
                currentList[index - 4 + 2] = currentList[index - 8 + 2]
                currentList[index - 4 + 3] = currentList[index - 8 + 3]

                currentList[index - 8] = currentList[index - 12]
                currentList[index - 8 + 1] = currentList[index - 12 + 1]
                currentList[index - 8 + 2] = currentList[index - 12 + 2]
                currentList[index - 8 + 3] = currentList[index - 12 + 3]

                currentList[index - 12] = currentList[index - 16]
                currentList[index - 12 + 1] = currentList[index - 16 + 1]
                currentList[index - 12 + 2] = currentList[index - 16 + 2]
                currentList[index - 12 + 3] = currentList[index - 16 + 3]

                currentList[index - 16] = repository.getRandomItem()
                currentList[index - 16 + 1] = repository.getRandomItem()
                currentList[index - 16 + 2] = repository.getRandomItem()
                currentList[index - 16 + 3] = repository.getRandomItem()
                _list.postValue(currentList)
            }
        }
    }

    private fun removeHorizontalRow(index: Int) {
        val currentList = _list.value!!.toMutableList()
        when (index) {
            in (0..3) -> {
                currentList[index] = repository.getRandomItem()
                currentList[index + 1] = repository.getRandomItem()
                currentList[index + 2] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (4..7) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]

                currentList[index - 4] = repository.getRandomItem()
                currentList[index - 4 + 1] = repository.getRandomItem()
                currentList[index - 4 + 2] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (8..11) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]

                currentList[index - 4] = currentList[index - 8]
                currentList[index - 4 + 1] = currentList[index - 8 + 1]
                currentList[index - 4 + 2] = currentList[index - 8 + 2]

                currentList[index - 8] = repository.getRandomItem()
                currentList[index - 8 + 1] = repository.getRandomItem()
                currentList[index - 8 + 2] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (12..15) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]

                currentList[index - 4] = currentList[index - 8]
                currentList[index - 4 + 1] = currentList[index - 8 + 1]
                currentList[index - 4 + 2] = currentList[index - 8 + 2]

                currentList[index - 8] = currentList[index - 12]
                currentList[index - 8 + 1] = currentList[index - 12 + 1]
                currentList[index - 8 + 2] = currentList[index - 12 + 2]

                currentList[index - 12] = repository.getRandomItem()
                currentList[index - 12 + 1] = repository.getRandomItem()
                currentList[index - 12 + 2] = repository.getRandomItem()
                _list.postValue(currentList)
            }

            in (16..19) -> {
                currentList[index] = currentList[index - 4]
                currentList[index + 1] = currentList[index - 4 + 1]
                currentList[index + 2] = currentList[index - 4 + 2]

                currentList[index - 4] = currentList[index - 8]
                currentList[index - 4 + 1] = currentList[index - 8 + 1]
                currentList[index - 4 + 2] = currentList[index - 8 + 2]

                currentList[index - 8] = currentList[index - 12]
                currentList[index - 8 + 1] = currentList[index - 12 + 1]
                currentList[index - 8 + 2] = currentList[index - 12 + 2]

                currentList[index - 12] = currentList[index - 16]
                currentList[index - 12 + 1] = currentList[index - 16 + 1]
                currentList[index - 12 + 2] = currentList[index - 16 + 2]

                currentList[index - 16] = repository.getRandomItem()
                currentList[index - 16 + 1] = repository.getRandomItem()
                currentList[index - 16 + 2] = repository.getRandomItem()
                _list.postValue(currentList)
            }
        }
    }

    private fun checkVerticalRowFirst(index: Int): Boolean {
        val currentList = _list.value!!.toMutableList()
        return (currentList[index + 4].bgValue == currentList[index].bgValue) &&
                (currentList[index + 4].imgValue == currentList[index].imgValue) &&
                (currentList[index + 8].bgValue == currentList[index].bgValue) &&
                (currentList[index + 8].imgValue == currentList[index].imgValue)
    }

    private fun checkVerticalRowSecond(index: Int): Boolean {
        val currentList = _list.value!!.toMutableList()
        return (currentList[index - 4].bgValue == currentList[index].bgValue) &&
                (currentList[index - 4].imgValue == currentList[index].imgValue) &&
                (currentList[index + 4].bgValue == currentList[index].bgValue) &&
                (currentList[index + 4].imgValue == currentList[index].imgValue)
    }

    private fun checkVerticalRowThird(index: Int): Boolean {
        val currentList = _list.value!!.toMutableList()
        return (currentList[index - 4].bgValue == currentList[index].bgValue) &&
                (currentList[index - 4].imgValue == currentList[index].imgValue) &&
                (currentList[index - 8].bgValue == currentList[index].bgValue) &&
                (currentList[index - 8].imgValue == currentList[index].imgValue)
    }


    private fun checkHorizontalRow(index: Int): Boolean {
        val currentList = _list.value!!.toMutableList()
        return (currentList[index + 1].bgValue == currentList[index].bgValue) &&
                (currentList[index + 1].imgValue == currentList[index].imgValue) &&
                (currentList[index + 2].bgValue == currentList[index].bgValue) &&
                (currentList[index + 2].imgValue == currentList[index].imgValue)
    }
}