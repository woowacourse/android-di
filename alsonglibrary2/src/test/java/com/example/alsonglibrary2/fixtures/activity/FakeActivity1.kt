package com.example.alsonglibrary2.fixtures.activity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel1

class FakeActivity1 : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel1> {
        AutoDIManager.createViewModelFactory<FakeViewModel1>()
    }
}
