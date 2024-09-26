package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.FieldInject

interface ToBeInjected

class FirstDependency : ToBeInjected

class SecondDependency : ToBeInjected

class InjectOwner {
    @FieldInject
    @Qualifier("first")
    lateinit var firstDependency: ToBeInjected

    @FieldInject
    @Qualifier("second")
    lateinit var secondDependency: ToBeInjected
}
