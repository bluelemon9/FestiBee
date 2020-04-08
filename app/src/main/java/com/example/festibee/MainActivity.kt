package com.example.festibee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gotolocal.setOnClickListener{view ->
            val nextIntent = Intent(this,LocalMainActivity::class.java)
            startActivity(nextIntent)
        }

        searchBtn.setOnClickListener{view ->
            //val nextIntent = Intent(this,)
            //nextIntent.putExtra("code","${BigAreaCode.bigareaCode}")
        }
    }
}
