package com.example.alsonglibrary2

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager

class FakeActivity : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel> {
        AutoDIManager.createViewModelFactory<FakeViewModel>()
    }
}