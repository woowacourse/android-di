package woowacourse.shopping

import android.app.Application
import com.example.bbottodi.di.Container
import com.example.bbottodi.di.annotation.InDisk
import com.example.bbottodi.di.annotation.InMemory
import com.example.bbottodi.di.annotation.Inject
import com.example.bbottodi.di.inject.AutoDependencyInjector.inject
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.di.DefaultModule.provideCartProductDao
import woowacourse.shopping.di.DefaultModule.provideInDiskCartRepository
import woowacourse.shopping.di.DefaultModule.provideInMemoryCartRepository
import woowacourse.shopping.di.DefaultModule.provideProductRepository
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initContainer()
    }

    private fun initContainer() {
        Container.apply {
            addInstance(CartProductDao::class, provideCartProductDao(applicationContext))
            addInstance(ProductRepository::class, ::provideProductRepository)
            addInstance(
                CartRepository::class,
                listOf(Inject::class.simpleName!!, InDisk::class.simpleName!!),
                inject(provideInDiskCartRepository()),
            )
            addInstance(
                CartRepository::class,
                listOf(Inject::class.simpleName!!, InMemory::class.simpleName!!),
                inject(provideInMemoryCartRepository()),
            )
        }
    }
}
