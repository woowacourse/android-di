package com.example.alsonglibrary2.fixtures.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.FieldInject
import com.example.alsonglibrary2.fixtures.DefaultFakeRepository0
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class FieldInjectedWithQualifierAndNoProviderFakeViewModel : ViewModel() {
    @FieldInject
    @DefaultFakeRepository0
    lateinit var fakeRepository: FakeRepository
}
