package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.FieldInject
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FieldInjectedWithoutQualifierFakeViewModel : ViewModel() {
    @FieldInject
    lateinit var fakeRepository: FakeRepository
}
