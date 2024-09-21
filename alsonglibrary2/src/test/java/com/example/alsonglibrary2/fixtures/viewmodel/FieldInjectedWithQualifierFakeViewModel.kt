package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.FieldInject
import com.example.alsonglibrary2.fixtures.DefaultFakeRepository1
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FieldInjectedWithQualifierFakeViewModel : ViewModel() {
    @FieldInject
    @DefaultFakeRepository1
    lateinit var fakeRepository: FakeRepository
}
