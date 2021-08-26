package com.example.candy.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.candy.R
import com.example.candy.databinding.FragmentChallengeDetailBinding


class ChallengeDetailFragment: Fragment() {

    private lateinit var challengeDetailBinding: FragmentChallengeDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        challengeDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_detail,container,false)

        return challengeDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        // Setting Title Bar
        challengeDetailBinding.titleBar.title.text = "챌린지 소개"




        // 챌린지 아이디 받아오기
    }
}