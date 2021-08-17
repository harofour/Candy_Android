package com.example.candy.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.adapter.HomeChallengeOngoingRecyclerAdapter
import com.example.candy.data.DataHomeChallengeOngoing
import com.example.candy.databinding.ItemChallengeRecyclerviewPossiblechallengeBinding
import com.example.candy.databinding.ItemHomeRecyclerviewMychallengeBinding
import com.example.candy.model.data.Challenge

class PossibleChallengeRecyclerAdapter(  private var dataSet: List<Challenge>
): RecyclerView.Adapter<PossibleChallengeRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ItemChallengeRecyclerviewPossiblechallengeBinding) :
        RecyclerView.ViewHolder(binding.root) {  // 어떤 view 에서 생성된 바인딩 인지 root에 담고 있다
        //val myTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            // myTextView = view.findViewById(R.id.tv_todo_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossibleChallengeRecyclerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge_recyclerview_possiblechallenge, parent, false)

        var params = view.layoutParams
        params.height = parent.measuredHeight / 8

        return MyViewHolder(ItemChallengeRecyclerviewPossiblechallengeBinding.bind(view))
    }

    override fun onBindViewHolder(myViewholder: MyViewHolder, position: Int) {

        myViewholder.binding.tvCategoryName.text = dataSet[position].category
        myViewholder.binding.tvRequiredScore.text = dataSet[position].requiredScore.toString()
        myViewholder.binding.tvTitle.text = dataSet[position].title
        myViewholder.binding.tvSubtitle.text = dataSet[position].subTitle

        var isLike = dataSet[position].likeDone
        /*if(isLike){  좋아요 처리하기

        }
        else{

        }*/

        // 챌린지 리스트 중 하나 누를 시 해당 챌린지 세부 페이지 이동할 것임
        myViewholder.binding.root.setOnClickListener {

        }



    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateList(newList: List<Challenge>){
        dataSet = newList
        notifyDataSetChanged()
    }
}