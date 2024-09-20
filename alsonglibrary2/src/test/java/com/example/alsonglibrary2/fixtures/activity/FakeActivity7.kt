package com.example.alsonglibrary2.fixtures.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.createAutoDIViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.FakeViewModel7

class FakeActivity7 : AppCompatActivity() {
    val viewModel by createAutoDIViewModel<FakeViewModel7>()
}
