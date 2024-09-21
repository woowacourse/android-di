package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.fixtures.DefaultFakeRepository1
import com.example.alsonglibrary2.fixtures.DefaultFakeRepository2
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class ConstructorInjectedWithTwoQualifierFakeViewModel(
    @DefaultFakeRepository1 val fakeRepository1: FakeRepository,
    @DefaultFakeRepository2 val fakeRepository2: FakeRepository,
) : ViewModel()
