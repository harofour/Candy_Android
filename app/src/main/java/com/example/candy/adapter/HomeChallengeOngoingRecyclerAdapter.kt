package com.example.candy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.data.DataHomeChallengeOngoing
import com.example.candy.databinding.ItemHomeRecyclerviewCategoryBinding
import com.example.candy.databinding.ItemHomeRecyclerviewMychallengeBinding

class HomeChallengeOngoingRecyclerAdapter(
        private var dataSet: List<DataHomeChallengeOngoing>
): RecyclerView.Adapter<HomeChallengeOngoingRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ItemHomeRecyclerviewMychallengeBinding) :
            RecyclerView.ViewHolder(binding.root) {  // 어떤 view 에서 생성된 바인딩 인지 root에 담고 있다
        //val myTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            // myTextView = view.findViewById(R.id.tv_todo_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeChallengeOngoingRecyclerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_recyclerview_mychallenge, parent, false)

        var params = view.layoutParams
        params.height = parent.measuredHeight / 5

        return MyViewHolder(ItemHomeRecyclerviewMychallengeBinding.bind(view))
    }

    override fun onBindViewHolder(myViewholder: MyViewHolder, position: Int) {

        myViewholder.binding.tvCategoryName.text = dataSet[position].category
        myViewholder.binding.tvTitle.text = dataSet[position].title
        myViewholder.binding.tvDescription.text = dataSet[position].description
        myViewholder.binding.tvCandy.text = dataSet[position].assigned_candy.toString()
        myViewholder.binding.tvRequiredScore.text = dataSet[position].required_score.toString()


        // 챌린지 리스트 중 하나 누를 시 해당 챌린지 세부 페이지 이동할 것임
        myViewholder.binding.root.setOnClickListener {

        }



    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}