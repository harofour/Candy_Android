package com.example.candy.challenge.adapter.categoryRecyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ItemChallengeRecyclerviewCategoryBinding
import com.example.candy.databinding.ItemHomeRecyclerviewCategoryBinding

class ChallengeCategoryRecyclerAdapter(
    private var dataSet: List<String>
): RecyclerView.Adapter<ChallengeCategoryRecyclerAdapter.MyViewHolder>(){

    class MyViewHolder(val binding: ItemChallengeRecyclerviewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {  // 어떤 view 에서 생성된 바인딩 인지 root에 담고 있다
        //val myTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            // myTextView = view.findViewById(R.id.tv_todo_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge_recyclerview_category, parent, false)

        var params = view.layoutParams
        params.height = parent.measuredHeight / 5 * 4


        return MyViewHolder(ItemChallengeRecyclerviewCategoryBinding.bind(view))
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        val categoryName = dataSet[position]
        myViewHolder.binding.tvCategoryChallenge.text = categoryName

        // 카테고리 클릭 시
        myViewHolder.binding.root.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    



}