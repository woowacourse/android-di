package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.woowa.di.ApplicationContext
import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import javax.inject.Qualifier


@InstallIn(SingletonComponent::class)
object DaoBinder {
    @Database
    fun provideCartProductDao(
        @Database database: ShoppingDatabase
    ): CartProductDao = database.cartProductDao()

}
