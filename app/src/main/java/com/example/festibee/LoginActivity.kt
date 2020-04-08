package com.example.festibee

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.start.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null

    private val RC_SIGN_IN = 900

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)

        auth = FirebaseAuth.getInstance()

        // 구글 로그인 토큰 설정
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // googleSignInClient 생성해서 gmail_button 리스너에서 gmail 인증 요청
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        gmail_login.setOnClickListener { view ->
            // 구글 로그인 버튼
            // googleSignInClient가 인증요청 관련 Intent를 생성해준다.
            var signInIntent: Intent? = googleSignInClient?.getSignInIntent()
            // Intent 받아서 gmail 인증요청
            googleLogin()
            //startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        joinBtn.setOnClickListener {view ->
            startActivity(Intent(this, JoinActivity::class.java))
        }
        emailLoginBtn.setOnClickListener{view ->
            emailLogin()
        }

    }

    private fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // 구글 로그인 성공 했을 때 결과값이 넘어오는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 구글에서 승인된 정보를 가지고 오기
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // 구글 로그인이 성공 했을 경우
                var account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
                // moveMainPage(auth?.currentUser) //유저아이디를 넘겨준다.
                Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_LONG).show()
            }
        }
    }


    // 구글 로그인 성공시 토큰값을 파이어베이스로 넘겨주어서 계정을 생성하는 콛,
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        var credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //다음 페이지 호출
                    moveMainPage(auth?.currentUser)
                }
            }
    }

    private fun emailLogin() {
        if (edtEmail.text.toString().isNullOrEmpty() || edtPassword.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            return
        }

        var email = edtEmail.text.toString()
        var password = edtPassword.text.toString()
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email 로그인 성공", Toast.LENGTH_LONG).show()
                    moveMainPage(auth?.currentUser)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}



