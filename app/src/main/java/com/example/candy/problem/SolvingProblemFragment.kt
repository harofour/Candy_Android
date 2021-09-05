package com.example.candy.problem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.R
import com.example.candy.databinding.FragmentSolvingProblemBinding
import com.example.candy.model.data.ProblemList
import com.example.candy.model.data.ProblemSolveDto
import com.example.candy.model.data.SettingProblem
import com.example.candy.model.data.SolveProblem
import com.example.candy.problem.adapter.IOnItemClickInterface
import com.example.candy.problem.adapter.ProblemAdapter
import com.example.candy.problem.viewmodel.ProblemViewModel
import com.example.candy.utils.CurrentUser
import kotlin.properties.Delegates

class SolvingProblemFragment : Fragment(), IOnItemClickInterface {
    val TAG: String = "SolvingProblemFragment"
    private var _binding: FragmentSolvingProblemBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val problemViewModel: ProblemViewModel by activityViewModels()
    private lateinit var positionAdapter: ProblemAdapter
    private var challengeId by Delegates.notNull<Int>()

    // 문제 리스트
    private var settingProblemList = ArrayList<SettingProblem>()

    // 현재 문제 번호
    private var currentProblemNumber = 0

    // 입력된 정답을 저장할 리스트
    private lateinit var isInputUserAnswer: MutableList<Boolean>

    // 문제 풀이 API 에 사용할 List
    private var problemSolve = ArrayList<SolveProblem>()

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
        navController = Navigation.findNavController(view)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    androidx.appcompat.app.AlertDialog.Builder(view.context)
                        .setTitle("주의")
                        .setMessage("문제풀기를 종료하시겠습니까?")
                        .setPositiveButton("확인") { _, _ ->
                            requireActivity().finish()
                        }
                        .setNegativeButton("취소") { _, _ -> }
                        .create()
                        .show()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback);

        initProblem()
        initProblemPosition()

        initButton()
        initRecyclerView()
    }


    /**
     * Problem position 이 변경 될 때마다 뷰에 문제가 바뀌고
     * 입력된 정답이 정답 리스트에 저장되고
     * 답 입력창은 초기화 된다.
     */
    private fun initProblemPosition() {
        problemViewModel.position.observe(viewLifecycleOwner, {
            binding.problem = settingProblemList[it]
            binding.problemAnswerET.setText("")
            if (settingProblemList[it].userAnswer != null)
                binding.problemAnswerET.setText(settingProblemList[it].userAnswer)
        })
    }

    private fun addUserAnswer(pos: Int) {
        // 입력된 정답을 리스트에 저장
        if (binding.problemAnswerET.text.toString() == "") {
            return
        } else {
            isInputUserAnswer[pos] = true
            settingProblemList[pos].userAnswer = binding.problemAnswerET.text.toString()
        }
    }

    private fun initProblem() {
        challengeId = problemViewModel.challengeId!!
        val data = HashMap<String, Int>()
        data["challengeId"] = challengeId
        problemViewModel.getProblem(CurrentUser.userToken!!, data).observe(viewLifecycleOwner, {
            setProblemData(it)
            problemViewModel.updatePosition(0)
            isInputUserAnswer = MutableList(settingProblemList.size) { false }
        })
        problemViewModel.initScoredScore()
    }

    private fun initRecyclerView() {
        positionAdapter = ProblemAdapter(binding.root.context, this)
        binding.problemRecyclerView.apply {
            adapter = positionAdapter
            layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onBtnClicked(position: Int) {
        addUserAnswer(problemViewModel.position.value!!)
        problemViewModel.updatePosition(position)
    }

    private fun setProblemData(it: ProblemList?) {
        it?.problem?.forEach {
            this.settingProblemList.add(
                SettingProblem(
                    it.problemNumber.toString(),
                    "Q${it.problemNumber}.  ${it.question}",
                    it.content,
                    it.problemId,
                    it.choiceList,
                    it.answer,
                    it.score,
                    null
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
            if (problemViewModel.position.value == settingProblemList.size - 1) {
                addUserAnswer(problemViewModel.position.value!!)
                return@setOnClickListener
            } else {
                addUserAnswer(problemViewModel.position.value!!)
                problemViewModel.addPosition()
                positionAdapter.updateItemColor(problemViewModel.position.value!!)
            }
        }
        binding.prevProblemTV.setOnClickListener {
            if (problemViewModel.position.value == 0) {
                addUserAnswer(problemViewModel.position.value!!)
                return@setOnClickListener
            } else {
                addUserAnswer(problemViewModel.position.value!!)
                problemViewModel.minusPosition()
                positionAdapter.updateItemColor(problemViewModel.position.value!!)
            }
        }

        // 채점
        binding.markButton.setOnClickListener {
            addUserAnswer(problemViewModel.position.value!!)
            if (isInputUserAnswer.contains(false)) {
                AlertDialog.Builder(binding.root.context)
                    .setTitle("풀리지 않은 문제가 있습니다.")
                    .setMessage("${isInputUserAnswer.indexOf(false) + 1}번 문제의 정답이 입력되지 않았습니다.")
                    .setPositiveButton("${isInputUserAnswer.indexOf(false) + 1}번으로 이동") { _, _ ->
                        problemViewModel.updatePosition(isInputUserAnswer.indexOf(false))
                        positionAdapter.updateItemColor(isInputUserAnswer.indexOf(false))
                    }
                    .create()
                    .show()
                return@setOnClickListener
            }

            settingProblemList.forEachIndexed { index, settingProblem ->
                if (settingProblem.userAnswer == settingProblem.problemAnswer) {
                    // 정답
                    problemViewModel.addScore(settingProblem.problemScore)
                    problemSolve.add(
                        SolveProblem(
                            settingProblem.problemId,
                            settingProblem.problemScore
                        )
                    )
                } else {
                    // 오답
                    problemSolve.add(
                        SolveProblem(
                            settingProblem.problemId,
                            0
                        )
                    )
                    problemViewModel.addWrongAnswer(settingProblem.problemId)
                }
            }
            // 문제 풀이 API 보내기
            val problemSolveDto = ProblemSolveDto(challengeId, problemSolve)
            problemViewModel.postProblemSolve(CurrentUser.userToken!!, problemSolveDto)


            navController.navigate(R.id.action_solvingProblemFragment_to_markingProblemFragment)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}