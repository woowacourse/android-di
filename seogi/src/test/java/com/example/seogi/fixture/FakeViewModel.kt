package com.example.seogi.fixture

import androidx.lifecycle.ViewModel
import com.example.seogi.di.annotation.FieldInject

class FakeViewModel(
    @Child1 val childFoo: ParentFoo,
) : ViewModel() {
    @FieldInject
    private lateinit var foo1: String

    private lateinit var foo2: String

    private val foo3: String = "foo2"
}

class FakeViewModel2(
    @Child2 val childFoo: ParentFoo,
) : ViewModel() {
    @FieldInject
    private lateinit var foo1: String

    private lateinit var foo2: String

    private val foo3: String = "foo2"
}
