package olive.di.fixture

import olive.di.annotation.Inject

class FieldInjectTest {
    @Inject
    lateinit var injectFoo: Foo
    lateinit var notInjectFoo: Foo

    fun isInitializedInjectFoo(): Boolean {
        return this::injectFoo.isInitialized
    }

    fun isInitializedNotInjectFoo(): Boolean {
        return this::notInjectFoo.isInitialized
    }
}
