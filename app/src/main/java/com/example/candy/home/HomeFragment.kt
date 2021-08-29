package com.example.candy.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.R
import com.example.candy.adapter.HorizontalItemDecorator
import com.example.candy.adapter.VerticalItemDecorator
import com.example.candy.challenge.ChallengeLectureActivity
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.adapter.CategoryAdapter
import com.example.candy.home.adapter.MyChallengeAdapter
import com.example.candy.model.viewModel.SharedViewModel

class HomeFragment : Fragment() {
    private lateinit var mAdapter: MyChallengeAdapter
    private lateinit var cAdapter: CategoryAdapter
    private var homeBinding: FragmentHomeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return with(modelClass) {
                    when {
                        isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel.getInstance()
                        else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
                    }
                } as T
            }
        }).get(HomeViewModel::class.java)
    }
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(SharedViewModel::class.java) -> SharedViewModel.getInstance() as T
                    else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
                }
            }
        }).get(SharedViewModel::class.java)
    }
    private lateinit var navController: NavController

    companion object {
        private lateinit var instance: HomeFragment

        @MainThread
        fun getInstance(): HomeFragment {
            instance = if (::instance.isInitialized) instance else HomeFragment()
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding = binding

        sharedViewModel.getCandyStudent().observe(viewLifecycleOwner, {
            homeBinding!!.tvMyCandy.text = getString(R.string.myCandy, it)
            Log.d("HomeFragment", "$it")
        })


        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        cAdapter = CategoryAdapter { pos -> onCategoryItemClicked(pos) }
        mAdapter = MyChallengeAdapter { pos -> onChallengeItemClicked(pos) }

        initCategory()
        initOnGoingChallenge()
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "onResume()")
    }


    private fun initCategory() {
        homeBinding!!.rvCategory.apply {
            adapter = cAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecorator(15))
            setHasFixedSize(true)
        }

        homeViewModel.getCategories().observe(viewLifecycleOwner) { data ->
            //update ui
            Log.d("HomeFragment", "getCategories / $data")
            cAdapter.setCategories(data)
        }
    }

    private fun initOnGoingChallenge() {
        homeBinding!!.rvChallenge.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalItemDecorator(15))
            setHasFixedSize(true)
        }

        homeViewModel.getOnGoingChallenges().observe(viewLifecycleOwner) { data ->
            //update ui
            Log.d("HomeFragment", "getCategories / $data")
            mAdapter.setChallenges(data)
        }
    }

    private fun onCategoryItemClicked(position: Int) {
        Log.d("HomeFragment", "CategoryItemClicked() position $position")
        homeViewModel.sortChallengeByCategory(position)
        // 진행중인 챌린지 리스트 정렬
    }

    private fun onChallengeItemClicked(position: Int) {
        Log.d("HomeFragment", "ChallengeItemClicked() position $position")
        // 강의 화면으로 이동
        val challenge = homeViewModel.getChallenge(position)
        val intent =
            Intent(requireActivity().applicationContext, ChallengeLectureActivity::class.java)
        intent.putExtra("Challenge", challenge)
        requireActivity().startActivity(intent)
    }

    override fun onDestroyView() {
//        homeBinding = null
        super.onDestroyView()
    }
}