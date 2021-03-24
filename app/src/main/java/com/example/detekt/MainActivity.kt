package com.example.detekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun nice() {
        val data = intent.getStringExtra("")
        crazy()
    }

    fun bro() {
        "".toString()
        "".toString()
        "".toString()
        "".toString()
    }

    fun crazy() {
        "".toString()
        "".toString()
        "".toString()
        bro()
        bro()
        bro()
        bro()
        bro()
        bro()
    }
}
