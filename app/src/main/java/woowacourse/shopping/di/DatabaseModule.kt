package woowacourse.shopping.di

import android.content.Context
import com.example.di.ActivityLifespan
import com.example.di.Dependency
import com.example.di.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DatabaseModule(
    private val context: Context,
) : Module {
    @Dependency
    @ActivityLifespan
    fun cartProductDao(): CartProductDao = ShoppingDatabase.instance(context).cartProductDao()
}
