package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class ConstructorRecursiveInjectedFakeViewModel(
    val fakeRepository: FakeRepository,
) : ViewModel()
