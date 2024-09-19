package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.FieldInject
import com.example.alsonglibrary2.fixtures.DefaultRepository
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FakeViewModel4 : ViewModel() {
    @FieldInject
    @DefaultRepository
    lateinit var fakeRepository: FakeRepository
}
