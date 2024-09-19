package com.example.alsonglibrary2.fixtures.activity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel5

class FakeActivity5 : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel5> {
        AutoDIManager.createViewModelFactory<FakeViewModel5>()
    }
}
