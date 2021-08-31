package com.example.candy.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.databinding.ItemHomeRecyclerviewCategoryBinding

class CategoryAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categories: List<String> = listOf()
    private var currentCategory: String = "전체"

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
    }

    override fun getItemCount(): Int = categories.size

    fun setCategories(categories: List<String>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    fun changeCategory(selectedPosition: Int){
        currentCategory = categories[selectedPosition]
    }
    fun getCurrentCategory(): String{
        return currentCategory
    }

    fun getCategory(position: Int): String {
        return categories[position]
    }

    inner class ViewHolder(private val binding: ItemHomeRecyclerviewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tvCategoryName.setOnClickListener { onItemClicked(layoutPosition) }
        }

        fun bind(category: String) {
            binding.tvCategoryName.text = category
        }
    }
}