package com.grid.game.domain.game

import com.grid.game.core.library.random
import com.grid.game.domain.game.adapter.GameItem
import kotlin.random.Random

class GameRepository {
    fun generateList(): List<GameItem> {
        val listToReturn = mutableListOf<GameItem>()
        repeat(20) {
            listToReturn.add(GameItem(Random.nextInt(), 1 random 3, Random.nextBoolean()))
        }
        return listToReturn
    }

    fun getRandomItem(): GameItem {
        return GameItem(Random.nextInt(), 1 random 3, Random.nextBoolean())
    }
}