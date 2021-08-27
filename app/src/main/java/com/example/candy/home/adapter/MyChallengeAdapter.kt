package com.example.candy.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.databinding.ItemHomeRecyclerviewMychallengeBinding
import com.example.candy.model.data.Challenge

class MyChallengeAdapter(
    private val itemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<MyChallengeAdapter.ViewHolder>() {

    private var challenges: List<Challenge> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeRecyclerviewMychallengeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(challenges[position])
    }

    override fun getItemCount(): Int = challenges.size

    fun setChallenges(challenges: List<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemHomeRecyclerviewMychallengeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.moveToLectureBtn.setOnClickListener {
                itemClicked(layoutPosition)
            }
        }

        fun bind(challenge: Challenge) {
            binding.title = challenge.title
            binding.subTitle = challenge.subTitle
            binding.category = challenge.category
            binding.candy = 11111
            binding.requiredScore = 11111
        }
    }
}