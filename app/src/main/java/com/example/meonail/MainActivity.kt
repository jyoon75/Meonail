package com.example.meonail

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.meonail.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_calendar, R.id.navigation_memory, R.id.navigation_home, R.id.navigation_wish, R.id.navigation_mypage
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration) //액션 바 이름 설정
        navView.setupWithNavController(navController)


        // 후기 작성 버튼 클릭 이벤트
        binding.btnRecordRegist.setOnClickListener {
            // RecordRegistActivity로 이동
            val intent = Intent(this, RecordRegistActivity::class.java)
            startActivity(intent)
        }
    }
}