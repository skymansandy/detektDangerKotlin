package com.example.detekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun directIntentCall() {
        val intent = intent
        intent.getBundleExtra("")
        intent.extras // should throw warning too
    }

    private fun fakeDirectIntentCall() {
        val fakeIntent = FakeIntent()
        fakeIntent.getStringExtra()
    }

    fun crazy() {
        "".toString()
        "".toString()
        "".toString()
    }
}
