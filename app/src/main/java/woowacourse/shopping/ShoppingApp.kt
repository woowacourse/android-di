package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.singleOf
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
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

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
    single<ProductRepository> { InMemoryProductRepository() }

    viewModelOf(::MainViewModel)
    viewModelOf(::CartViewModel)

    // dateFormat (presentation)
//    scope<CartActivity> {
//        scoped<DateFormatter> { KoreanLocaleDateFormatter(get()) }
//    }

//    scope<CartActivity> {
//        scoped<DateFormatter> { (activityContext: Context) -> KoreanLocaleDateFormatter(activityContext) }
//    }

    scope<CartActivity> {
        scoped<DateFormatter> { KoreanLocaleDateFormatter(get()) }
    }
}

