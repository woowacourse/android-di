package woowacourse.shopping

import android.app.Application
import android.content.Context
import com.example.di.Component
import com.example.di.DiContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartProductDao
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.annotation.ActivityContext
import woowacourse.shopping.di.annotation.ApplicationContext
import woowacourse.shopping.di.registerApplicationContext
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.data.annotation.InMemoryCartProductDao as InMemoryCartProductDaoQualifier

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerApplicationContext(applicationContext)

        DiContainer.bindProvides(
            type = CartProductDao::class,
            installIn = { Component.Singleton },
            isScoped = true,
            factoryFunction = { DatabaseModule.cartProductDao(applicationContext) }
        )

        DiContainer.bindProvides(
            type = CartProductDao::class,
            installIn = { Component.Singleton },
            isScoped = true,
            qualifier = InMemoryCartProductDaoQualifier::class,
            factoryFunction = { InMemoryCartProductDao() }
        )

        DiContainer.bindProvides(
            type = Context::class,
            installIn = { owner -> Component.Activity(owner!!) },
            isScoped = true,
            qualifier = ActivityContext::class,
        ) { owner -> owner as Context }

        DiContainer.bindProvides(
            type = Context::class,
            installIn = { Component.Singleton },
            isScoped = true,
            qualifier = ApplicationContext::class,
        ) { _ -> applicationContext }

        DiContainer.bindBinds(
            fromInterface = CartRepository::class,
            toImplementation = DefaultCartRepository::class,
            installIn = { Component.Singleton },
            isScoped = true,
        )

        DiContainer.bindBinds(
            fromInterface = ProductRepository::class,
            toImplementation = DefaultProductRepository::class,
            installIn = { owner -> Component.ViewModel(owner!!) },
            isScoped = true,
        )
    }
}
