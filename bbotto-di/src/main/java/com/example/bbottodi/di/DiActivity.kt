package com.example.bbottodi.di

import androidx.appcompat.app.AppCompatActivity

open class DiActivity : AppCompatActivity() {
    val container: ContainerWithContext by lazy {
        ContainerWithContext((application as DiApplication).container, this)
    }
}
