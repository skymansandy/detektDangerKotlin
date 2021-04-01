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
        string as FakeIntent
        string as? FakeIntent
        string as List<String>
        string as? List<String>
        string as? List<FakeIntent>
        string as List<FakeIntent?>
        string as? List<FakeIntent?>
        string as List<Map<String, FakeIntent>>
        string as List<Map<String, FakeIntent?>>
        string as Map<String, List<FakeIntent?>>
        string as Map<String, List<FakeIntent?>?>
        launchActivity<OtherActivity>()
        launchActivity<FakeIntent>()
        startActivity(Intent(this, OtherActivity::class.java))
    }
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
