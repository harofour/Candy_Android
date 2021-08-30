package com.example.candy.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.candy.R
import com.example.candy.databinding.ActivityMainBinding
import com.example.candy.model.data.User
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.utils.CurrentUser
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val Tag = "MainActivity"
    private var mainBinding: ActivityMainBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        mainBinding = binding
        setContentView(mainBinding!!.root)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment? ?: return
        val navController = host.navController


        setupBottomNavMenu(navController)

        // 로그인 후 유저 정보 저장
        CurrentUser.userInfo = intent.getSerializableExtra("userInfo") as User
        CurrentUser.userToken = "Bearer ${intent.getStringExtra("userToken")}"
        sharedViewModel.setUserPw(intent.getStringExtra("userPw") ?: "1234")
        Log.d(Tag, ".\n userInfo : ${CurrentUser.userInfo}   \n userToken : ${CurrentUser.userToken}")

        // 학생 캔디 받아오기
        sharedViewModel.getAPICandyStudent(CurrentUser.userToken!!)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }
}