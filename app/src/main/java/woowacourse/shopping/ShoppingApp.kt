package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.LocalCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.local.ShoppingDatabase
import woowacourse.shopping.presentation.MainViewModel
import woowacourse.shopping.presentation.cart.CartActivity
import woowacourse.shopping.presentation.cart.CartViewModel
import woowacourse.shopping.presentation.cart.DateFormatter
import woowacourse.shopping.presentation.cart.KoreanLocaleDateFormatter

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShoppingApp)
            androidLogger(level = Level.DEBUG)
            modules(myAppModules)
        }
    }
}

val myAppModules = module {
    // DB & DAO (local)
    single(createdAtStart = true) {
        Room.databaseBuilder(
            get(),
            ShoppingDatabase::class.java, ShoppingDatabase.NAME
        ).build()
    }
    single { get<ShoppingDatabase>().cartProductDao() }

    // Repository (data)
    single<CartRepository> { LocalCartRepository(get()) }

    // TODO: viewModelScope 로
//    single<ProductRepository> { InMemoryProductRepository() }

    viewModelOf(::MainViewModel)
    viewModelOf(::CartViewModel)

    scope<MainViewModel> {
//        scoped<ProductRepository> { InMemoryProductRepository() }
        scopedOf(::InMemoryProductRepository).bind<ProductRepository>()
    }

    // dateFormat (presentation)
    scope<CartActivity> {
        scoped<DateFormatter> { (activityContext: Context) -> KoreanLocaleDateFormatter(activityContext) }
    }
}

