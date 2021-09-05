package com.example.candy.problem

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.candy.R
import com.example.candy.databinding.FragmentMarkingFragmentBinding
import com.example.candy.problem.viewmodel.ProblemViewModel


class MarkingProblemFragment : Fragment() {
    private var _binding : FragmentMarkingFragmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val problemViewModel: ProblemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarkingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initButton()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    androidx.appcompat.app.AlertDialog.Builder(view.context)
                        .setTitle("주의")
                        .setMessage("채점화면을 종료하시겠습니까?")
                        .setPositiveButton("확인") { _, _ ->
                            requireActivity().finish()
                        }
                        .setNegativeButton("취소"){_,_->}
                        .create()
                        .show()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback);

        binding.scoredScore.text = getString(R.string.challengeScore,problemViewModel.getScoredScore())

        binding.totalProblem.text = problemViewModel.getProblemSize().toString()
        binding.rightAnswer.text = problemViewModel.getRightAnswerSize().toString()
        binding.wrongAnswer.text = problemViewModel.getWrongAnswerSize().toString()

    }

    private fun initButton() {
        binding.finishButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.retryButton.setOnClickListener {
            navController.navigate(R.id.action_markingProblemFragment_to_solvingProblemFragment)
        }
        binding.ViewWrongQuestions.setOnClickListener {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}