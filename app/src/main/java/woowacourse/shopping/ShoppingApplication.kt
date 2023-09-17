package woowacourse.shopping

import android.app.Application
import com.example.bbottodi.di.Container
import com.example.bbottodi.di.annotation.InDisk
import com.example.bbottodi.di.annotation.InMemory
import com.example.bbottodi.di.annotation.Inject
import com.example.bbottodi.di.inject.AutoDependencyInjector.inject
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InDiskCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        localDatabase = ShoppingDatabase.getInstance(this)
        initContainer()
    }

    private fun initContainer() {
        Container.apply {
            addInstance(CartProductDao::class, localDatabase.cartProductDao())
            addInstance(ProductRepository::class, inject(DefaultProductRepository::class))
            addInstance(
                CartRepository::class,
                listOf(Inject::class.simpleName!!, InDisk::class.simpleName!!),
                inject(InDiskCartRepository::class),
            )
            addInstance(
                CartRepository::class,
                listOf(Inject::class.simpleName!!, InMemory::class.simpleName!!),
                inject(InMemoryCartRepository::class),
            )
        }
    }

    companion object {
        lateinit var localDatabase: ShoppingDatabase
    }
}
