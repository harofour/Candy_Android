package com.example.candy.challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.candy.R
import com.example.candy.databinding.FragmentChallengeLectureBinding

class ChallengeLectureFragment : Fragment() {
    private var _binding: FragmentChallengeLectureBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeLectureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // Setting Title
        binding.titleBar.title.text = "챌린지 강의"
        binding.titleBar.backBtn.setOnClickListener {
            navController.popBackStack()
        }

        arguments?.get("Challenge").let{
            Log.d("ChallengeLectureFragment","Challenge argement / $it")
        }

        // 임시 좋아요 아이콘
        Glide.with(binding.root).load(R.drawable.icon_challenge_like_empty)
            .into(binding.titleBar.favoriteIv)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}