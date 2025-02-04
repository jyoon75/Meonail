package com.example.meonail.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meonail.MainActivity
import com.example.meonail.databinding.ActivityLoginBinding
import com.example.meonail.R
import com.example.meonail.util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 액션바 제목 설정
        supportActionBar?.title = "Meonail"

        // ViewBinding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UI 요소 변수 할당
        val username = binding.username  // 사용자 이메일 입력 필드
        val password = binding.password  // 비밀번호 입력 필드
        val login = binding.login        // 로그인 버튼
        val loading = binding.loading    // 로딩 인디케이터

        // ViewModel 초기화
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        // 로그인 폼 상태 관찰 (입력값 검증)
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // 로그인 버튼 활성화 여부 설정
            login.isEnabled = loginState.isDataValid

            // 사용자명 입력 오류 처리
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            // 비밀번호 입력 오류 처리
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        // 로그인 결과 관찰
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            // 로딩 인디케이터 숨기기
            loading.visibility = View.GONE

            // 로그인 실패 처리
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }

            // 로그인 성공 처리
            if (loginResult.success != null) {
                updateUiWithUser()
            }

            setResult(Activity.RESULT_OK)
            finish()
        })

        // 사용자명 입력값 변경 감지 및 로그인 폼 상태 업데이트
        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            // 비밀번호 입력값 변경 감지 및 로그인 폼 상태 업데이트
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            // 키보드에서 "완료" 버튼을 눌렀을 때 로그인 시도
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            // 로그인 버튼 클릭 시 로그인 실행
            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    /**
     * 로그인 성공 시 UI 업데이트 및 메인 화면 이동
     */
    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)

        // 사용자가 입력한 이메일 가져오기
        val email = binding.username.text.toString()

        // 이메일에서 "@" 앞부분만 추출 (예: "user@example.com" → "user")
        val nickname = email.substringBefore("@")

        // 환영 메시지 표시
        Toast.makeText(
            applicationContext,
            "$welcome $nickname",
            Toast.LENGTH_LONG
        ).show()

        // 로그인 상태 저장
        val sessionManager = SessionManager(this)
        sessionManager.saveLoginState(true)
        sessionManager.saveNickname(nickname) // 닉네임 저장

        // 메인 액티비티로 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * 로그인 실패 시 오류 메시지 표시
     */
    private fun showLoginFailed(errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * EditText에 afterTextChanged 확장 함수 추가
 * 입력값 변경을 감지하고 특정 동작을 수행할 수 있도록 함
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


