package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.LocalCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.local.ShoppingDatabase
import woowacourse.shopping.presentation.MainViewModel
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

// 아래는 koin

// TODO:  ProductRepository 를 ViewModelScope 로 변경.
val myAppModules = module {
    // DB & DAO (local)
    single {
        Room.databaseBuilder(
            get(),
            ShoppingDatabase::class.java, "shopping.db"
        ).build()
    }
    single { get<ShoppingDatabase>().cartProductDao() }

    // Repository (data)
    single<CartRepository> { LocalCartRepository(get()) }
    single<ProductRepository> { InMemoryProductRepository() }


    // viewModel (presentation)
    viewModel { MainViewModel(get(), get()) }
    viewModel { CartViewModel(get()) }

    // dateFormat (presentation)
    single<DateFormatter> { KoreanLocaleDateFormatter(get()) }
}

