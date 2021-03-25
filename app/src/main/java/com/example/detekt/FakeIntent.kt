package com.example.detekt

class FakeIntent {
    fun getStringExtra() {
        print("fake getStringExtra")
    }

    fun getExtras() {
        print("Just testing getExtras")
    }

    val extras = ""
}
