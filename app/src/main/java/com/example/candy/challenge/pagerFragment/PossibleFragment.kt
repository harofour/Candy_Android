package com.example.candy.challenge.pagerFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.candy.databinding.FragmentLikeChallengeBinding
import com.example.candy.databinding.FragmentPossibleChallengeBinding


class PossibleFragment: Fragment() {

    private var possibleChallengeBinding: FragmentPossibleChallengeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentPossibleChallengeBinding.inflate(inflater, container, false)
        possibleChallengeBinding = binding

        return possibleChallengeBinding!!.root
    }



    override fun onDestroyView() {
        possibleChallengeBinding = null
        super.onDestroyView()
    }
}