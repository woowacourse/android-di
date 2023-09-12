package woowacourse.shopping.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.HasDiContainer
import woowacourse.shopping.data.CartInDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.ContextType.Type.ACTIVITY
import woowacourse.shopping.di.StorageType.Type.DATABASE
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.cart.DateFormatter

class DiActivityModule(
    parentDiContainer: DiContainer?,
    private val context: Context,
) : DiContainer(parentDiContainer) {
    @ContextType(ACTIVITY)
    fun provideContext(): Context = context

    @StorageType(DATABASE)
    fun provideCartInDiskRepository(
        cartProductDao: CartProductDao,
    ): CartRepository = CartInDiskRepository(cartProductDao)

    fun provideCartProductDao(
        shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()

    fun provideDateFormatter(
        @ContextType(ACTIVITY) context: Context,
    ): DateFormatter = DateFormatter(context)

    companion object {
        fun create(activity: AppCompatActivity): DiActivityModule {
            return DiActivityModule(
                (activity.application as? HasDiContainer)?.diContainer,
                activity,
            )
        }
    }
}
