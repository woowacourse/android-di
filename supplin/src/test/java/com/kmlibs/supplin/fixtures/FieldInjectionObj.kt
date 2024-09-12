package com.kmlibs.supplin.fixtures

import com.kmlibs.supplin.annotations.Supply

class FieldInjectionObj {
    @Supply
    @FooQualifier2
    lateinit var foo: Foo
}
