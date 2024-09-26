package com.example.alsonglibrary2.fixtures.viewmodel

import com.example.alsonglibrary2.di.LifecycleViewModel
import com.example.alsonglibrary2.di.anotations.ViewModelScope
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

class LifecycleFakeViewModel : LifecycleViewModel() {
    @ViewModelScope
    lateinit var fakeRepository: FakeRepository

    fun alsongClear() {
        super.onCleared()
    }
}

