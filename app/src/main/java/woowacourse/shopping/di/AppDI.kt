package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import woowacourse.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase

object AppDI {
    private val container =
        DependencyContainer(
            bindings = mapOf(CartRepository::class to DefaultCartRepository::class),
        )
    private val factory = ReflectionViewModelFactory(container)

    val viewModelFactory: ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                container.register(CartProductDao::class) {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    Room
                        .databaseBuilder(application, ShoppingDatabase::class.java, "shopping.db")
                        .build()
                        .cartProductDao()
                }
                return factory.create(modelClass)
            }
        }
}
