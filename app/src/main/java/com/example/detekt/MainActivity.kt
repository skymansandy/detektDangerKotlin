package com.example.detekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.detekt.internal.Foo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val foo = Foo()
        foo.toString()
    }

    fun  nice(){

    }

    fun bro(){
        "".toString()
        "".toString()
        "".toString()
    }

    fun crazy(){
        "".toString()
        "".toString()
        "".toString()
        bro()
        bro()
    }
}
