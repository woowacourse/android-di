package com.example.alsonglibrary2.fixtures.activity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel4

class FakeActivity4 : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel4> {
        AutoDIManager.createViewModelFactory<FakeViewModel4>()
    }
}
