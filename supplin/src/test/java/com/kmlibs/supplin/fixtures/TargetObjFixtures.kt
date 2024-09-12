package com.kmlibs.supplin.fixtures

import com.kmlibs.supplin.annotations.Supply

interface Foo

class Foo1 : Foo

class Foo2 : Foo

class Foos(
    @FooQualifier1
    private val foo1: Foo,
    @FooQualifier2
    private val foo2: Foo,
)

class FieldFoo {
    @Supply
    @FooQualifier1
    lateinit var foo1: Foo

    @Supply
    @FooQualifier2
    lateinit var foo2: Foo
}
