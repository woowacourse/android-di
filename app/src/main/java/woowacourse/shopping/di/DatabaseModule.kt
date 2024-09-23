package woowacourse.shopping.di

import android.content.Context
import com.kmlibs.supplin.ApplicationScopeContainer
import com.kmlibs.supplin.annotations.ApplicationContext
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.model.Scope
import woowacourse.shopping.data.ShoppingDatabase

@Module
@Within(Scope.Application::class)
object DatabaseModule {
    @Concrete
    fun provideShoppingDatabase(
        @ApplicationContext applicationContext: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(applicationContext)
}
