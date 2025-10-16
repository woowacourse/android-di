package com.example.di.wrapper

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

abstract class DIActivity : AppCompatActivity() {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory by lazy {
        DIViewModelFactory((application as DIApplication).container)
    }
}
