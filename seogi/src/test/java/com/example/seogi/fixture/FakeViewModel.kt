package com.example.seogi.fixture

import com.example.seogi.di.DiViewModel
import com.example.seogi.di.annotation.FieldInject

class FakeViewModel : DiViewModel() {
    @FieldInject
    @Child1
    lateinit var childFoo: ParentFoo

    private lateinit var foo2: String

    private val foo3: String = "foo2"

    fun onClearedViewModel() {
        super.onCleared()
    }
}

class FakeViewModel2(
    @Child1 val childFoo1: ParentFoo,
) : DiViewModel() {
    @FieldInject
    @Child2
    lateinit var childFoo2: ParentFoo

    private lateinit var foo2: String

    private val foo3: String = "foo2"
}
