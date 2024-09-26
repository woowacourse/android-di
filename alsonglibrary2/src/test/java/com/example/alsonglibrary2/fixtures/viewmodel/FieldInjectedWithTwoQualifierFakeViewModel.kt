package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.anotations.FieldInject
import com.example.alsonglibrary2.fixtures.DefaultFakeRepository1
import com.example.alsonglibrary2.fixtures.DefaultFakeRepository2
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FieldInjectedWithTwoQualifierFakeViewModel : ViewModel() {
    @FieldInject
    @DefaultFakeRepository1
    lateinit var fakeRepository1: FakeRepository

    @FieldInject
    @DefaultFakeRepository2
    lateinit var fakeRepository2: FakeRepository
}
