package di.fixture

import com.example.di.Injected

class FakeComponentA

class ConstructorInjectedClass(
    @Injected val constructorInjected: FakeComponentA,
)

class FieldInjectedClass {
    @Injected
    lateinit var fieldInjected: FakeComponentA
}
