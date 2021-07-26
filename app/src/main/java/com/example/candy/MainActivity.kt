package com.example.candy

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.candy.candy.CandyFragment
import com.example.candy.challenge.ChallengeFragment
import com.example.candy.databinding.ActivityMainBinding
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.HomeFragment
import com.example.candy.myPage.MyPageFragment
import com.example.candy.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private var mainBinding: ActivityMainBinding? = null

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var  homeFrgment : HomeFragment
    private lateinit var  challengeFragment : ChallengeFragment
    private lateinit var  candyFragment : CandyFragment
    private lateinit var  myPageFragment : MyPageFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        mainBinding = binding

        setContentView(mainBinding!!.root)


        mainBinding?.bottomNavView?.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        homeFrgment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.framelayout_main, homeFrgment).commit()






    }

    // 바텀네비게이션 아이템 클릭 리스너 설정
    private val onBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {

        when(it.itemId){
            R.id.item_home -> {

                Log.d(TAG, "MainActivity - 홈 탭 클릭!")
                homeFrgment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, homeFrgment).commit()
            }
            R.id.item_candy -> {


                Log.d(TAG, "MainActivity -  캔디 관리 탭 클릭!")
                candyFragment = CandyFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, candyFragment).commit()
            }
            R.id.item_challenge -> {

                Log.d(TAG, "MainActivity - 챌린지 탭 클릭!")
                challengeFragment = ChallengeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, challengeFragment).commit()
            }

            R.id.item_myPage -> {

                Log.d(TAG, "MainActivity - 마이페이지 탭 클릭!")
                myPageFragment = MyPageFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, myPageFragment).commit()
            }
        }

        true
    }

}