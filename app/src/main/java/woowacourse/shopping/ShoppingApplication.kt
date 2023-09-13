package woowacourse.shopping

import android.app.Application
import com.example.pingudi.Container
import com.example.pingudi.Injector
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InDiskCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Container.addInstance(InMemoryProductRepository::class, null)
        Container.addInstance(InDiskCartRepository::class, null)
        Container.addInstance(InMemoryCartRepository::class, null)
        Container.addInstance(
            CartProductDao::class,
            ShoppingDatabase.getDatabase(this).cartProductDao(),
        )

        injector = Injector(Container)
    }

    companion object {
        lateinit var injector: Injector
    }
}
