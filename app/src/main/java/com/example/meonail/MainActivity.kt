package com.example.meonail

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.meonail.databinding.ActivityMainBinding
import com.example.meonail.ui.login.LoginActivity
import com.example.meonail.util.SessionManager

class  MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // ViewBinding 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SessionManager를 사용하여 로그인 상태 확인
        val sessionManager = SessionManager(this)
        val isLoggedIn = sessionManager.isLoggedIn()

        // 로그로 현재 로그인 상태 출력 (디버깅용)
        Log.d("MainActivity", "로그인 상태: $isLoggedIn")

        if (!isLoggedIn) {
            // 로그인 상태가 아니면 LoginActivity로 이동
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // MainActivity 종료
            return // 이후 코드는 실행하지 않도록 return
        }

        // 로그인 상태일 경우 메인 화면 로직 실행
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation() // 하단 네비게이션 설정
        setupListeners() // 버튼 등의 리스너 설정
    }

    /**
     * 하단 네비게이션 초기화 및 설정
     */
    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main) // NavController 가져오기
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_calendar,
                R.id.navigation_memory,
                R.id.navigation_home,
                R.id.navigation_wish,
                R.id.navigation_mypage
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration) // 액션 바와 NavController 연결
        binding.navView.setupWithNavController(navController) // 네비게이션 뷰 연결
    }

    /**
     * 버튼 및 기타 이벤트 리스너 설정
     */
    private fun setupListeners() {
        // 버튼 클릭 시 액션 설정
        binding.btnRecordRegist.setOnClickListener {
            if (!checkPermission()) { // 권한 체크
                // 권한이 있을 경우 RecordRegistActivity로 이동
                val intent = Intent(this, RecordRegistActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * 권한 체크 로직
     * 외부 저장소/미디어 접근 권한을 확인하고, 필요하면 요청
     */
    private fun checkPermission(): Boolean {
        val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.READ_MEDIA_IMAGES
        else
            android.Manifest.permission.READ_EXTERNAL_STORAGE

        // 권한이 없는 경우
        if (ContextCompat.checkSelfPermission(this, readImagePermission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, readImagePermission)) {
                // 권한 설명 대화상자를 보여줌
                val dlg = AlertDialog.Builder(this)
                dlg.setTitle("미디어 접근 권한")
                dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필요합니다.")
                dlg.setPositiveButton("확인") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(readImagePermission),
                        REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(readImagePermission),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
            }
            return false
        }
        return true // 권한이 이미 허용된 상태
    }

    companion object {
        private const val REQUEST_READ_EXTERNAL_STORAGE = 1000 // 권한 요청 코드
    }
}



