package com.example.candy.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ChallengelistItemViewLoadingBinding
import com.example.candy.databinding.ItemChallengeRecyclerviewPossiblechallengeBinding
import com.example.candy.model.data.Challenge

class PossibleChallengeRecyclerAdapter(
        private var dataSet: ArrayList<Challenge>,
        val touchLikeImage: (challenge: Challenge, index: Int) -> Unit
        ,val selectChallenge:(challenge: Challenge) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1


    class ItemViewHolder(val binding: ItemChallengeRecyclerviewPossiblechallengeBinding) :
        RecyclerView.ViewHolder(binding.root) {  // 어떤 view 에서 생성된 바인딩 인지 root에 담고 있다

    }

    inner class LoadingViewHolder(private val binding: ChallengelistItemViewLoadingBinding) :
            RecyclerView.ViewHolder(binding.root) {

    }

    // 아이템 뷰의 타입을 정한다
    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position].title) {
            " " -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                //val layoutInflater = LayoutInflater.from(parent.context)
                //val binding = ItemChallengeRecyclerviewPossiblechallengeBinding.inflate(layoutInflater, parent, false)

                val view =LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_challenge_recyclerview_possiblechallenge, parent, false)

                ItemViewHolder(ItemChallengeRecyclerviewPossiblechallengeBinding.bind(view))
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChallengelistItemViewLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if(holder is ItemViewHolder) {
            var safePosition = holder.adapterPosition
            holder.binding.tvCategoryName.text = dataSet[safePosition].category
            holder.binding.tvRequiredScore.text = dataSet[safePosition].requiredScore.toString()
            holder.binding.tvTitle.text = dataSet[safePosition].title
            holder.binding.tvSubtitle.text = dataSet[safePosition].subTitle


            var isLike = dataSet[safePosition].likeDone
            if(isLike){  //찜 여부 처리하기
                holder.binding.ivLike.setImageResource(R.drawable.icon_challenge_like_filled)
            }
            else{
                holder.binding.ivLike.setImageResource(R.drawable.icon_challenge_like_empty)
            }

            // 하트 누를 시 찜 활성화 or 비활성화
            holder.binding.ivLike.setOnClickListener {
                touchLikeImage.invoke(dataSet[safePosition], safePosition)
            }


            // 챌린지 리스트 중 하나 누를 시 해당 챌린지 세부 페이지 이동할 것임
            holder.binding.root.setOnClickListener {
               selectChallenge.invoke(dataSet[safePosition])
            }

        }
        else{  // 로딩 아이템 뷰

        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }



    fun updateList(newList: ArrayList<Challenge>){
        /*dataSet.addAll(newList)
        dataSet.add(Challenge(-9999," ",false,0,0," "," "))

        notifyDataSetChanged()*/
        if(!dataSet.isEmpty()) // 최초 호출 시 인덱스 범위 에러 방지
            deleteLoading()

        dataSet.addAll(newList)

        notifyDataSetChanged()
    }

    fun addLoading(){
        dataSet.add(Challenge(-9999," ",false,0,0," "," ",-1))

        notifyItemInserted(dataSet.lastIndex)
        //notifyDataSetChanged()
    }

    fun deleteLoading(){
        val lastIndex = dataSet.lastIndex
        dataSet.removeAt(dataSet.lastIndex)
        notifyItemRemoved(lastIndex)
    }

    fun getLastChallengeId(index: Int): Int{
        return dataSet[index].challengeId
    }

    fun dataSetClear(){
        dataSet.clear()
    }


}