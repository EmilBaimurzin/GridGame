package com.grid.game.domain.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.grid.game.R
import com.grid.game.core.library.l
import com.grid.game.databinding.ItemGameBinding
import java.util.Collections




class GameAdapter(private var itemClicked: (Int) -> Unit) : RecyclerView.Adapter<GameViewHolder>() {
    var items = mutableListOf<GameItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemClicked = itemClicked
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }
}

class GameViewHolder(private val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
    var itemClicked: ((Int) -> Unit)? = null
    fun bind(item: GameItem) {
        val bg = if (item.bgValue) R.drawable.bg_item_1 else R.drawable.bg_item_2
        val itemImg = when (item.imgValue) {
            1 -> R.drawable.img_symbol01
            2 -> R.drawable.img_symbol02
            else -> R.drawable.img_symbol03
        }
        binding.itemImg.setImageResource(itemImg)
        binding.itemImg.setBackgroundResource(bg)
        binding.selectedView.isVisible = item.isSelected
        binding.itemImg.setOnClickListener {
            itemClicked?.invoke(adapterPosition)
        }
    }
}