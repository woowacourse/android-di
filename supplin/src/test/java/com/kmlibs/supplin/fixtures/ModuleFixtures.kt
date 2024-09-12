package com.kmlibs.supplin.fixtures

import com.kmlibs.supplin.annotations.Module

@Module
object Module1 {
    @FooQualifier1
    fun provideFoo1(): Foo = Foo1()
}

@Module
object Module2 {
    @FooQualifier2
    fun provideFoo(): Foo = Foo2()
}
