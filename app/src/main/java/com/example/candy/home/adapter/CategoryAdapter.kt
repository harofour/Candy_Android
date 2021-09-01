package com.example.candy.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ItemHomeRecyclerviewCategoryBinding

class CategoryAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categories: List<String> = listOf()
    private var currentCategory: String = "전체"

    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeRecyclerviewCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])

        // recyclerview item 배경색 변경
        if (selectedPosition == position) {
            holder.binding.tvCategoryName.setBackgroundResource(R.drawable.rect_item_selected)
        } else {
            holder.binding.tvCategoryName.setBackgroundResource(R.drawable.rect_item_unselected)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun setCategories(categories: List<String>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    fun setCurrentCategory(selectedPosition: Int) {
        currentCategory = categories[selectedPosition]
    }

    fun getCurrentCategory(): String {
        return currentCategory
    }

    fun updateItemColor(p: Int) {
        this.notifyItemChanged(selectedPosition)
        selectedPosition = p
        this.notifyItemChanged(selectedPosition)

    }

    inner class ViewHolder(val binding: ItemHomeRecyclerviewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tvCategoryName.setOnClickListener {
                if (layoutPosition != selectedPosition) {
                    onItemClicked(layoutPosition)
                    updateItemColor(layoutPosition)
                }
            }
        }


        fun bind(category: String) {
            binding.tvCategoryName.text = category
        }
    }
}