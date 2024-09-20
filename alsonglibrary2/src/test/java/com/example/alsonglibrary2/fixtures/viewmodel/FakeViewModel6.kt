package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.FieldInject
import com.example.alsonglibrary2.fixtures.DefaultRepository1
import com.example.alsonglibrary2.fixtures.DefaultRepository2
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FakeViewModel6 : ViewModel() {
    @FieldInject
    @DefaultRepository1
    lateinit var fakeRepository1: FakeRepository

    @FieldInject
    @DefaultRepository2
    lateinit var fakeRepository2: FakeRepository
}
