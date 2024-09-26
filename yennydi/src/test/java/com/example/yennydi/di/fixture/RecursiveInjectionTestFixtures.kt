package com.example.yennydi.di.fixture

import com.example.yennydi.di.Injected

class SecondConstructorInjectedComponent

class FirstConstructorInjectedComponent(
    @Injected val constructorInjected: SecondConstructorInjectedComponent,
)

class RecursiveConstructorInjectedClass(
    @Injected val constructorInjected: FirstConstructorInjectedComponent,
)

class SecondFieldInjectedComponent

class FirstFieldInjectedComponent {
    @Injected
    lateinit var fieldInjected: SecondFieldInjectedComponent
}

class RecursiveFieldInjectedClass {
    @Injected
    lateinit var fieldInjected: FirstFieldInjectedComponent
}
