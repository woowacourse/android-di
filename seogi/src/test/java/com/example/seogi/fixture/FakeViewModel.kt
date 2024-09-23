package com.example.seogi.fixture

import androidx.lifecycle.ViewModel
import com.example.seogi.di.annotation.FieldInject

class FakeViewModel : ViewModel() {
    @FieldInject
    @Child1
    lateinit var childFoo: ParentFoo

    private lateinit var foo2: String

    private val foo3: String = "foo2"
}

class FakeViewModel2(
    @Child1 val childFoo1: ParentFoo,
) : ViewModel() {
    @FieldInject
    @Child2
    lateinit var childFoo2: ParentFoo

    private lateinit var foo2: String

    private val foo3: String = "foo2"
}
