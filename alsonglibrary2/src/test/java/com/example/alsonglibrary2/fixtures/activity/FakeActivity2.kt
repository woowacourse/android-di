package com.example.alsonglibrary2.fixtures.activity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel2

class FakeActivity2 : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel2> {
        AutoDIManager.createViewModelFactory<FakeViewModel2>()
    }
}
