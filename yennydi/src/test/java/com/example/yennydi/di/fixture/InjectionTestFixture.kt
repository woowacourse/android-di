package com.example.yennydi.di.fixture

import com.example.yennydi.di.Injected

class FakeComponentA

class ConstructorInjectedClass(
    @Injected val constructorInjected: FakeComponentA,
)

class FieldInjectedClass {
    @Injected
    lateinit var fieldInjected: FakeComponentA
}
