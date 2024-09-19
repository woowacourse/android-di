package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.fixtures.DefaultRepository
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FakeViewModel2(
    @DefaultRepository val fakeRepository: FakeRepository,
) : ViewModel()
