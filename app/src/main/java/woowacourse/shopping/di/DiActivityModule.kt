package woowacourse.shopping.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.InstanceHolder
import com.re4rk.arkdi.Qualifier
import woowacourse.shopping.data.CartInDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.util.HasViewModelFactory
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class DiActivityModule(
    parentDiContainer: DiContainer,
    private val context: Context,
) : DiContainer(parentDiContainer) {
    @Qualifier("ActivityContext")
    fun provideContext(): Context = Cache.context.get {
        context
    }

    @Qualifier("CartInDiskRepository")
    fun provideCartInDiskRepository(
        cartProductDao: CartProductDao,
    ): CartRepository = Cache.cartInDiskRepository.get {
        CartInDiskRepository(cartProductDao)
    }

    fun provideCartProductDao(
        shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = Cache.cartProductDao.get {
        shoppingDatabase.cartProductDao()
    }

    fun provideDateFormatter(
        @Qualifier("ActivityContext") context: Context,
    ): DateFormatter = Cache.dataFormatter.get {
        DateFormatter(context)
    }

    fun inject(activity: AppCompatActivity) {
        activity::class.declaredMemberProperties.filter { it.hasAnnotation<ArkInject>() }
            .forEach { property ->
                property.isAccessible = true
                property.javaField?.set(activity, get(property.returnType.jvmErasure))
            }

        val hasViewModelFactory = (activity as HasViewModelFactory)
        hasViewModelFactory.viewModelFactory = ViewModelFactory(this)
    }

    class ViewModelFactory(
        private val diContainer: DiContainer,
    ) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return diContainer.createInstance(modelClass.kotlin)
        }
    }

    private object Cache {
        val cartInDiskRepository = InstanceHolder<CartRepository>()
        val cartProductDao = InstanceHolder<CartProductDao>()
        val dataFormatter = InstanceHolder<DateFormatter>()
        val context = InstanceHolder<Context>()
    }
}
