package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.fixtures.DefaultFakeRepository1
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class ConstructorInjectedWithQualifierFakeViewModel(
    @DefaultFakeRepository1 val fakeRepository: FakeRepository,
) : ViewModel()
