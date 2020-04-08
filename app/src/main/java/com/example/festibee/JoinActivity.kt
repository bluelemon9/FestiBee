package com.example.festibee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = FirebaseAuth.getInstance()

        edtJoin2.setOnClickListener {
            createAndLoginEmail()
            finish()
        }
    }

    private fun createAndLoginEmail() {
        if (edtEmail2.text.toString().isNullOrEmpty() || edtPassword2.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            return
        }

        var email = edtEmail2.text.toString()
        var password = edtPassword2.text.toString()
        progressBar2.visibility = View.VISIBLE
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                progressBar2.visibility = View.GONE
                if (task.isSuccessful) {
                    //아이디 생성이 성공했을 경우
                    Toast.makeText(this,
                        "회원가입 성공",
                        Toast.LENGTH_LONG).show()
                } else {
                    //회원가입 에러가 발생했을 경우
                    Toast.makeText(this,
                        task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}
