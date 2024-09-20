package com.example.alsonglibrary2.fixtures.activity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel3

class FakeActivity3 : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel3> {
        AutoDIManager.createViewModelFactory<FakeViewModel3>()
    }
}
