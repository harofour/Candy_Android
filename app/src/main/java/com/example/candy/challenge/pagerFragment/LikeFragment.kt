package com.example.candy.challenge.pagerFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.candy.databinding.FragmentCompleteChallengeBinding
import com.example.candy.databinding.FragmentLikeChallengeBinding

class LikeFragment: Fragment() {

    private var likeChallengeBinding: FragmentLikeChallengeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentLikeChallengeBinding.inflate(inflater, container, false)
        likeChallengeBinding = binding

        return likeChallengeBinding!!.root
    }



    override fun onDestroyView() {
        likeChallengeBinding = null
        super.onDestroyView()
    }
}