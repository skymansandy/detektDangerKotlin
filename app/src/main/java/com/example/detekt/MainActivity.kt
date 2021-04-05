package com.example.detekt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
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
        getBundleExtra("")
//        intent.extras // should throw warning too
    }

    private fun fakeDirectIntentCall() {
        val fakeIntent = FakeIntent()
        fakeIntent.getStringExtra()
        fakeIntent.extras
    }

    fun getBundleExtra(string: String) {
        "".toString()
        var list: List<FakeIntent>? = null
        list as FakeIntent
        list as? FakeIntent
        list as List<String>
        list as? List<String>
        list as? List<FakeIntent>
        list as List<FakeIntent?>
        list as? List<FakeIntent?>
        list as List<Map<String, FakeIntent>>
        list as List<Map<String, FakeIntent?>>
        list as Map<String, List<FakeIntent?>>
        list as Map<String, List<FakeIntent?>?>
        launchActivity<OtherActivity>()
        launchActivity<FakeIntent>()
        startActivity(Intent(this, OtherActivity::class.java))
    }
/*
    fun spanExample() {
        val spannable = SpannableString("You can start learning Android from MindOrks")
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            36, // start
            44, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }*/
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)
