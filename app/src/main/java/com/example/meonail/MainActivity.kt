package com.example.meonail

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.meonail.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

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
            if(!checkPermission()){ // 사진 권한 참일 때
                // RecordRegistActivity로 이동
                val intent = Intent(this, RecordRegistActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkPermission(): Boolean {
        // 빌드 버전에 따라 권한 체크
        val readImagePermissioin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.READ_MEDIA_IMAGES
        else android.Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(this, readImagePermissioin) != PackageManager.PERMISSION_GRANTED){
            // 권한이 부여되지 않았다면
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, readImagePermissioin)){
                // 이미 거부된 적이 있다면
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("미디어 접근 권한")
                dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다.")
                dlg.setPositiveButton("확인") {dialog, which -> ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(readImagePermissioin),
                    REQUEST_READ_EXTERNAL_STORAGE)}
                dlg.setNegativeButton("취소", null)
                dlg.show()
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(readImagePermissioin),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
            return false
        } else {
            // 권한이 부여되었다면 참 리턴
            return true
        }
    }
}