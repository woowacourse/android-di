package com.example.seogi.fixture

import androidx.lifecycle.ViewModel
import com.example.seogi.di.annotation.FieldInject

class FakeViewModel : ViewModel() {
    @FieldInject
    private lateinit var foo1: String

    private lateinit var foo2: String

    private val foo3: String = "foo2"
}
