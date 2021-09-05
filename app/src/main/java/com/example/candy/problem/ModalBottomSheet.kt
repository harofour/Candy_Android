package com.example.candy.problem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.candy.R
import com.example.candy.databinding.ModalBottomSheetContentBinding
import com.example.candy.model.data.Problem
import com.example.candy.problem.viewmodel.ProblemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet : BottomSheetDialogFragment() {
    private val problemViewModel: ProblemViewModel by activityViewModels()
    private lateinit var binding: ModalBottomSheetContentBinding

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModalBottomSheetContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wrongProblem = problemViewModel.getWrongProblem()

        var problemPos = 0
        bind(wrongProblem[problemPos])
        binding.nextProblem.setOnClickListener {
            if (problemPos == wrongProblem.size - 1) {
                return@setOnClickListener
            } else {
                bind(wrongProblem[++problemPos])
            }
        }
        binding.prevProblem.setOnClickListener {
            if (problemPos == 0) {
                return@setOnClickListener
            } else {
                bind(wrongProblem[--problemPos])
            }
        }


    }

    fun bind(problem: Problem) {
        binding.problemNumberTv.text = getString(R.string.problemNumber, problem.problemNumber)
        binding.problemTitleTV.text = problem.question
        binding.problemContentTV.text = problem.content
        binding.problemAnswerET.text = problem.answer

    }
}