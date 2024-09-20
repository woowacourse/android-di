package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.FieldInject
import com.example.alsonglibrary2.fixtures.DefaultRepository0
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FakeViewModel5 : ViewModel() {
    @FieldInject
    @DefaultRepository0
    lateinit var fakeRepository: FakeRepository
}
