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
import com.example.candy.utils.HorizontalItemDecorator
import com.example.candy.challenge.ChallengeDetailActivity
import com.example.candy.challenge.adapter.LikeChallengeRecyclerAdapter
import com.example.candy.challenge.adapter.categoryRecyclerAdapter.LikeChallengeCategoryRecyclerAdapter
import com.example.candy.challenge.viewmodel.LikeChallengeViewModel
import com.example.candy.databinding.FragmentLikeChallengeBinding
import com.example.candy.model.data.Challenge
import com.example.candy.model.injection.Injection

class LikeFragment: Fragment() {

    private var likeChallengeBinding: FragmentLikeChallengeBinding? = null
    private val categoryList = arrayListOf<String>()  // 카테고리

    private lateinit var viewModel: LikeChallengeViewModel

  //  private lateinit var navController: NavController // 챌린지 선택 시 챌린지 소개 화면으로 넘어가기 위함

    private var page = 1 // 리스트 10개가 1page
    private var No_More_Data = false  // 서버에서 데이터 마지막까지 다 가져 오면 true 된다

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentLikeChallengeBinding.inflate(inflater, container, false)
        likeChallengeBinding = binding

        return likeChallengeBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("fragment check","LikeFragment onViewCreated")

        // navigation
       // navController = Navigation.findNavController(view)

        // 카테고리 recycler
        categoryList.add("ALL")
        categoryList.add("영어")
        categoryList.add("수학")
        categoryList.add("국어")
        categoryList.add("과학")
        categoryList.add("한국사")

        likeChallengeBinding!!.recyclerLikeChallengeCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        likeChallengeBinding!!.recyclerLikeChallengeCategory.adapter = LikeChallengeCategoryRecyclerAdapter(categoryList)
        likeChallengeBinding!!.recyclerLikeChallengeCategory.addItemDecoration(
            HorizontalItemDecorator(20)
        )


        // 찜 목록 리스트 recycler
        likeChallengeBinding!!.recyclerLikeChallenge.layoutManager = LinearLayoutManager(context)
        likeChallengeBinding!!.recyclerLikeChallenge.adapter =
                LikeChallengeRecyclerAdapter(
                        ArrayList<Challenge>(),
                        touchLikeImage={ x: Challenge, y: Int ->
                            // 하트 이미지 터치로 찜하기, 활성화 상태에서 다시 터치하면 찜 취소
                            viewModel.touchLikeImage(x.challengeId, x.likeDone)
                            x.likeDone = !x.likeDone
                            (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter).
                            deleteUnlikeList(y)
                        },
                        selectChallenge = {
                            // 챌린지 선택 시 해당 챌린지 소개 화면으로 이동
                            val intent = Intent(activity, ChallengeDetailActivity::class.java)
                            intent.putExtra("challengeId", it.challengeId)
                            intent.putExtra("lectureId", it.lectureId)
                            startActivity(intent)
                        }
                        )
       // likeChallengeBinding!!.recyclerLikeChallenge.addItemDecoration(VerticalItemDecorator(10))

        viewModel = ViewModelProvider(viewModelStore, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LikeChallengeViewModel(
                        Injection.provideRepoRepository(context!!)
                ) as T
            }
        }).get(LikeChallengeViewModel::class.java)

        // 찜 챌린지 리스트 observe
        viewModel.likeChallengeLiveData.observe(viewLifecycleOwner,{
            (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter).updateList(it)
            Log.d("api test check", "updateList called : update recycler list")

          /*  (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter).
            notifyItemRangeChanged((page-1) * it.size, it.size)
            Log.d("api test check", "notifyItemRangeChanged page : ${page}  it.size: ${it.size}") */

            // 마지막 목록이면 더 이상 데이터가 없으므로 progressbar 제거해주기!!
            /*if(it.size == 0){
                (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter).deleteLoading()
                Log.d("api test check","더 이상 데이터 없음")
                No_More_Data = true
            }*/
        })

        // progressbar observe
        viewModel.progressVisible.observe(viewLifecycleOwner, {
            if(it){
                likeChallengeBinding!!.progressbar.visibility = View.VISIBLE
            }
            else{
                likeChallengeBinding!!.progressbar.visibility = View.GONE
            }
        })

        // textViewVisble observe
        viewModel.totalLikeListLiveData.observe(viewLifecycleOwner,{
            if(it == 0)
                likeChallengeBinding!!.tvEmptyLikelist.visibility = View.VISIBLE
            else
                likeChallengeBinding!!.tvEmptyLikelist.visibility = View.GONE
        })





        // 리사클러뷰 무한스크롤 구현
        likeChallengeBinding!!.recyclerLikeChallenge.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                Log.d("api test check", "likeRecycler onScrollStateChanged called")

                val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalItemViewCount = recyclerView.adapter!!.itemCount-1

                if(newState == 2 && !recyclerView.canScrollVertically(1)
                        && lastVisibleItemPosition == totalItemViewCount){
                    Log.d("api test check", "like recycler 스크롤 마지막 도달로 인한 호출")

                    // 마지막 챌린지 아이디 받아오기
                    var lastChallengeId = (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter)
                            .getLastChallengeId(totalItemViewCount)

                    // 로딩 아이템 뷰 추가
                    (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter).addLoading()

                    // 요청
                    page++

                    Log.d("api test check", "lieke recycler 스크롤 마지막 호출 : lastChallengeId : ${lastChallengeId}")
                    viewModel.getAllLikeChallengeList(lastChallengeId, 10, false)



                }
            }

            /*
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d("api test check before super.onScrolled", "dy : ${dy}")
                super.onScrolled(recyclerView, dx, dy)

                Log.d("api test check ", "dy : ${dy}")

                Log.d("api test check", "likeRecycler onScroll")

                val lastVisibleItemViewPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalItemViewCount = recyclerView.adapter!!.itemCount-1

                Log.d("api test check", "LikeRecycler lastVisibleItemViewPosition : ${lastVisibleItemViewPosition}")
                Log.d("api test check", "LikeRecycler totalItemViewCount : ${totalItemViewCount}")

                // 스크롤 마지막에 도달 시
                if(lastVisibleItemViewPosition == totalItemViewCount && !No_More_Data){
                    (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter).deleteLoading()

                    var lastChallengeId = (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter)
                            .getLastChallengeId(totalItemViewCount - 1)

                    // 페이지 증가
                    page++

                    //Log.d("api test check", "스크롤 마지막 도달로 인한 호출")

                    viewModel.getAllLikeChallengeList(lastChallengeId, 10, false)

                }

            }

            */

        })






    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("fragment check", "LikeFragment onActivityCreated")
    }


    override fun onResume() {
        super.onResume()
        Log.d("api test check", "page : ${page}")
        Log.d("fragment check","LikeFragment onResume")

        page = 1
        No_More_Data = false


        (likeChallengeBinding!!.recyclerLikeChallenge.adapter as LikeChallengeRecyclerAdapter).dataSetClear()
        viewModel.getAllLikeChallengeList(100000000, 10, true)
    }


    override fun onDestroyView() {
        likeChallengeBinding = null
        super.onDestroyView()
    }
}