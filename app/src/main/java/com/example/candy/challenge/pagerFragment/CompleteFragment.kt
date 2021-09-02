package com.example.candy.challenge.pagerFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.candy.databinding.FragmentChallengeBinding
import com.example.candy.databinding.FragmentCompleteChallengeBinding

class CompleteFragment: Fragment() {

    private var completeChallengeBinding: FragmentCompleteChallengeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentCompleteChallengeBinding.inflate(inflater, container, false)
        completeChallengeBinding = binding

        return completeChallengeBinding!!.root
    }


    override fun onResume() {
        super.onResume()
        Log.d("fragment check", "CompleteFragment onResume")
    }

    override fun onDestroyView() {
        completeChallengeBinding = null
        super.onDestroyView()
    }

}