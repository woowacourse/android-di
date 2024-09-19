package com.example.alsonglibrary2.fixtures.activity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel6

class FakeActivity6 : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel6> {
        AutoDIManager.createViewModelFactory<FakeViewModel6>()
    }
}
