package com.example.seogi.fixture

import androidx.lifecycle.ViewModel
import com.example.seogi.di.annotation.FieldInject

class FakeViewModel : ViewModel() {
    @FieldInject
    lateinit var foo: String
}
