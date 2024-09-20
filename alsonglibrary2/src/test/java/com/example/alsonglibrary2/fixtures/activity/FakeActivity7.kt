package com.example.alsonglibrary2.fixtures.activity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel7

class FakeActivity7 : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel7> {
        AutoDIManager.createViewModelFactory<FakeViewModel7>()
    }
}
