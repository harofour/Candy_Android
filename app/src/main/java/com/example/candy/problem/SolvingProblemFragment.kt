package com.example.candy.problem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.R
import com.example.candy.databinding.FragmentSolvingProblemBinding
import com.example.candy.model.data.ProblemList
import com.example.candy.model.data.SettingProblem
import com.example.candy.problem.adapter.IOnItemClickInterface
import com.example.candy.problem.adapter.ProblemAdapter
import com.example.candy.problem.viewmodel.ProblemViewModel
import com.example.candy.utils.CurrentUser

class SolvingProblemFragment : Fragment(), IOnItemClickInterface {
    private var _binding : FragmentSolvingProblemBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val problemViewModel : ProblemViewModel by activityViewModels()
    private lateinit var positionAdapter : ProblemAdapter
    // 문제 리스트
    private var settingProblemList = ArrayList<SettingProblem>()
    // 현재 문제 번호
    private var currentProblemNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_solving_problem, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val challengeId : Int = problemViewModel.challengeId!!
        val data = HashMap<String,Int>()
        data["challengeId"] = challengeId
        problemViewModel.getProblem(CurrentUser.userToken!!,data).observe(viewLifecycleOwner,{
            setProblemData(it)
        })

        initButton()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        positionAdapter = ProblemAdapter(binding.root.context,this)
        binding.problemRecyclerView.apply {
            adapter = positionAdapter
            layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.HORIZONTAL,false)
        }
    }

    override fun onBtnClicked(position: Int) {
        currentProblemNumber = position
        binding.problem = settingProblemList[currentProblemNumber]
    }

    private fun setProblemData(it: ProblemList?) {
        it?.problem?.forEach {
            this.settingProblemList.add(
                SettingProblem(
                    it.ProblemNumber.toString(),
                    "Q${it.ProblemNumber}.  ${it.question}",
                    it.content,
                    it.answer,
                    it.choiceList
                )
            )
        }
        binding.problem = settingProblemList[currentProblemNumber]
        positionAdapter.setProblemSize(this.settingProblemList.size)
    }

    private fun initButton() {
        binding.finishButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.nextProblemTV.setOnClickListener {
            if(currentProblemNumber == settingProblemList.size - 1)
                return@setOnClickListener
            else{
                currentProblemNumber +=1
                positionAdapter.updateItemColor(currentProblemNumber)
                binding.problem = settingProblemList[currentProblemNumber]
            }
        }

        binding.prevProblemTV.setOnClickListener {
            if(currentProblemNumber == 0){
                return@setOnClickListener
            }else{
                currentProblemNumber -= 1
                positionAdapter.updateItemColor(currentProblemNumber)
                binding.problem = settingProblemList[currentProblemNumber]
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}