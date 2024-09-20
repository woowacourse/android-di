package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.fixtures.DefaultRepository1
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FakeViewModel2(
    @DefaultRepository1 val fakeRepository: FakeRepository,
) : ViewModel()
