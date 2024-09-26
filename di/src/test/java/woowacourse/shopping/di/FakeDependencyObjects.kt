package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.FieldInject
import javax.inject.Qualifier

@Qualifier
annotation class FirstQualifier

@Qualifier
annotation class SecondQualifier

@Qualifier
annotation class ThirdQualifier

interface ToBeInjected

class FirstDependency : ToBeInjected

class SecondDependency : ToBeInjected

class InjectOwner {
    @FieldInject
    @FirstQualifier
    lateinit var firstDependency: ToBeInjected

    @FieldInject
    @SecondQualifier
    lateinit var secondDependency: ToBeInjected
}
