package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.di.AppInjector
import woowacourse.shopping.di.Provider
import woowacourse.shopping.di.definition.DefinitionInformation
import woowacourse.shopping.di.definition.Kind
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule : InjectionModule {
    private fun registerDefinitions(): List<DefinitionInformation<*>> =
        listOf(
            DefinitionInformation(
                kclass = ProductRepository::class,
                qualifier = null,
                kind = Kind.SINGLETON,
                provider = { _ -> Provider { ProductRepositoryImpl() } },
            ),
            DefinitionInformation(
                kclass = CartRepository::class,
                qualifier = null,
                kind = Kind.SINGLETON,
                provider = { injector: AppInjector ->
                    Provider {
                        val dao: CartProductDao = injector.get()
                        CartRepositoryImpl(dao)
                    }
                },
            ),
        )

    override fun provideDefinitions(): List<DefinitionInformation<*>> = registerDefinitions()
}
