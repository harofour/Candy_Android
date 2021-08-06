package com.example.candy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ItemHomeRecyclerviewCategoryBinding

class HomeCategoryRecyclerAdapter(
    private var dataSet: List<String>
): RecyclerView.Adapter<HomeCategoryRecyclerAdapter.MyViewHolder>(){

    class MyViewHolder(val binding: ItemHomeRecyclerviewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {  // 어떤 view 에서 생성된 바인딩 인지 root에 담고 있다
        //val myTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            // myTextView = view.findViewById(R.id.tv_todo_content)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val view =LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_home_recyclerview_category, viewGroup, false)

        // 카테고리 텍스트 값에 따라 배경 색상 지정해주기
        //
        //

        return MyViewHolder(ItemHomeRecyclerviewCategoryBinding.bind(view))
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        val categoryName = dataSet[position]
        myViewHolder.binding.tvCategoryName.text = categoryName

        // 카테고리 클릭 시
        myViewHolder.binding.root.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }



}