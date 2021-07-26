package com.example.candy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ItemMainRecyclerviewCategoryBinding

class MainCategoryRecyclerAdapter(
    private var dataSet: List<String>
): RecyclerView.Adapter<MainCategoryRecyclerAdapter.MyViewHolder>(){

    class MyViewHolder(val binding: ItemMainRecyclerviewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {  // 어떤 view 에서 생성된 바인딩 인지 root에 담고 있다
        //val myTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            // myTextView = view.findViewById(R.id.tv_todo_content)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val view =LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_main_recyclerview_category, viewGroup, false)

        return MyViewHolder(ItemMainRecyclerviewCategoryBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}