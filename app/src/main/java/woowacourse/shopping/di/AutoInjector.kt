package woowacourse.shopping.di

import woowacourse.shopping.di.module.NormalModule
import woowacourse.shopping.di.module.SingletonModule

class AutoInjector(
    private val singletonModule: SingletonModule,
    private val normalModule: NormalModule,
) : Injector {
    override fun <T : Any> inject(): T {
        TODO("Not yet implemented")
    }
}
