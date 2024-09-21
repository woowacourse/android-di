package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.fixtures.DefaultRepository1
import com.example.alsonglibrary2.fixtures.DefaultRepository2
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class ConstructorInjectedWithTwoQualifierFakeViewModel(
    @DefaultRepository1 val fakeRepository1: FakeRepository,
    @DefaultRepository2 val fakeRepository2: FakeRepository,
) : ViewModel()
