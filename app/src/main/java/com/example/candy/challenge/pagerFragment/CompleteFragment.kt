package com.example.candy.challenge.pagerFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.challenge.ChallengeDetailActivity
import com.example.candy.challenge.adapter.CompleteChallengeRecyclerAdapter
import com.example.candy.challenge.adapter.PossibleChallengeRecyclerAdapter
import com.example.candy.challenge.adapter.categoryRecyclerAdapter.ChallengeCategoryRecyclerAdapter
import com.example.candy.challenge.adapter.categoryRecyclerAdapter.CompleteChallengeCategoryRecyclerAdapter
import com.example.candy.challenge.viewmodel.CompleteChallengeViewModel
import com.example.candy.challenge.viewmodel.PossibleChallengeViewModel
import com.example.candy.databinding.FragmentChallengeBinding
import com.example.candy.databinding.FragmentCompleteChallengeBinding
import com.example.candy.model.data.Challenge
import com.example.candy.model.data.ChallengeComplete
import com.example.candy.model.injection.Injection
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.HorizontalItemDecorator

class CompleteFragment: Fragment() {

    private var completeChallengeBinding: FragmentCompleteChallengeBinding? = null

    private val categoryList = arrayListOf<String>()  // 카테고리
    //private lateinit var possibleChallengeList : List<Challenge>
    private lateinit var viewModel: CompleteChallengeViewModel

    // 카테고리
    private var CurrentCategory = "전체"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentCompleteChallengeBinding.inflate(inflater, container, false)
        completeChallengeBinding = binding

        return completeChallengeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("fragment check","CompleteFragment onViewCreated")
        Log.d("token check", CurrentUser.userToken!!)

        completeChallengeBinding!!.recyclerCompleteChallengeCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        completeChallengeBinding!!.recyclerCompleteChallengeCategory.adapter = CompleteChallengeCategoryRecyclerAdapter(categoryList,
                { category -> onCategoryItemClicked(category) })
        completeChallengeBinding!!.recyclerCompleteChallengeCategory.addItemDecoration(
                HorizontalItemDecorator(20)
        )

        completeChallengeBinding!!.recyclerCompleteChallenge.layoutManager = LinearLayoutManager(context)
        completeChallengeBinding!!.recyclerCompleteChallenge.adapter =
                CompleteChallengeRecyclerAdapter(
                        ArrayList<ChallengeComplete>(),
                        selectChallenge={
                            // 챌린지 선택 시 해당 챌린지 소개 화면으로 이동
                            val intent = Intent(activity, ChallengeDetailActivity::class.java)
                            intent.putExtra("challengeId", it.challengeId)
                            intent.putExtra("lectureId", it.lectureId)
                            intent.putExtra("isCompleted", true)
                            startActivity(intent)

                        }
                )

        viewModel = ViewModelProvider(viewModelStore, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CompleteChallengeViewModel(
                        Injection.provideRepoRepository(context!!)
                ) as T
            }
        }).get(CompleteChallengeViewModel::class.java)

        // 카테고리 가져오기
        viewModel.getCategories().observe(viewLifecycleOwner){ data ->
            //update ui
            Log.d("api test check", "complete getCategories / $data")
            (completeChallengeBinding!!.recyclerCompleteChallengeCategory.adapter as CompleteChallengeCategoryRecyclerAdapter).setCategories(data)
        }

        // 도전 가능 챌린지 리스트 observe
        viewModel.completeChallengeLiveData.observe(viewLifecycleOwner,{
            (completeChallengeBinding!!.recyclerCompleteChallenge.adapter as CompleteChallengeRecyclerAdapter).updateList(it)

        })

        // progressbar observe
        viewModel.progressVisible.observe(viewLifecycleOwner, {
            if(it){
                completeChallengeBinding!!.progressbar.visibility = View.VISIBLE
            }
            else{
                completeChallengeBinding!!.progressbar.visibility = View.GONE
            }
        })

        // textViewVisble observe
        viewModel.totalCompleteListLiveData.observe(viewLifecycleOwner,{
            if(it == 0)
                completeChallengeBinding!!.tvComplete.visibility = View.VISIBLE
            else
                completeChallengeBinding!!.tvComplete.visibility = View.GONE
        })


        // 리사클러뷰 무한스크롤 구현
        completeChallengeBinding!!.recyclerCompleteChallenge.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                Log.d("api test check", "complete Recycler onScrollStateChanged called")

                val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalItemViewCount = recyclerView.adapter!!.itemCount - 1

                if (newState == 2 && !recyclerView.canScrollVertically(1)
                        && lastVisibleItemPosition == totalItemViewCount) {
                    Log.d("api test check", "complete recycler 스크롤 마지막 도달로 인한 호출")

                    // 마지막 챌린지 아이디 받아오기
                    var lastChallengeId = (completeChallengeBinding!!.recyclerCompleteChallenge.adapter as CompleteChallengeRecyclerAdapter)
                            .getLastChallengeId(totalItemViewCount)

                    // 로딩 아이템 뷰 추가
                    (completeChallengeBinding!!.recyclerCompleteChallenge.adapter as CompleteChallengeRecyclerAdapter).addLoading()


                    Log.d("api test check", "possible recycler 스크롤 마지막 호출 : lastChallengeId : ${lastChallengeId}")
                    viewModel.getAllCompleteChallengeList(lastChallengeId, 10, false, CurrentCategory)

                }
            }
        })



    }

    // 카테고리 선택 시 데이터를 다시 로드.
    private fun onCategoryItemClicked(category: String) {
        Log.d("api test check", "CategoryItemClicked() category : $category ")
        CurrentCategory = category

        (completeChallengeBinding!!.recyclerCompleteChallenge.adapter as CompleteChallengeRecyclerAdapter).dataSetClear()
        viewModel.getAllCompleteChallengeList(100000000, 10, true, CurrentCategory)

    }


    override fun onResume() {
        super.onResume()
        Log.d("fragment check", "CompleteFragment onResume")
        Log.d("api test check", "CompleteFragment Current Category $CurrentCategory")

        (completeChallengeBinding!!.recyclerCompleteChallenge.adapter as CompleteChallengeRecyclerAdapter).dataSetClear()
        viewModel.getAllCompleteChallengeList(100000000, 10, true, CurrentCategory)
    }

    override fun onDestroyView() {
        completeChallengeBinding = null
        super.onDestroyView()
    }

}