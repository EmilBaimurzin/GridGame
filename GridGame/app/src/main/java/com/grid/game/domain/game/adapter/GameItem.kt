package com.grid.game.domain.game.adapter

data class GameItem(
    val id: Int,
    val imgValue: Int,
    val bgValue: Boolean,
    var isSelected: Boolean = false
)
