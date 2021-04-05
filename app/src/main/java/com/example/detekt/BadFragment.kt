package com.example.detekt

import android.os.Bundle
import androidx.fragment.app.Fragment

class BadFragment(val badId: String) : Fragment() {

    lateinit var age: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = activity?.intent
        activity?.intent?.getBundleExtra("nice")
    }
}
