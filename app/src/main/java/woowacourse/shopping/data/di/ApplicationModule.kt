package woowacourse.shopping.data.di

import android.content.Context
import com.woowacourse.di.DependencyContainer
import com.woowacourse.di.Module
import com.woowacourse.di.annotation.QualifierType
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class ApplicationModule(private val context: Context) : Module {
    override fun install() {
        provideCartRepository()
    }

    override fun clear() {
        DependencyContainer.clear()
    }

    private fun provideCartRepository() {
        DependencyContainer.addInstance(
            classType = CartRepository::class,
            instance =
                DefaultCartRepository(
                    ShoppingDatabase.instance(context).cartProductDao(),
                ),
            qualifier = QualifierType.DATABASE,
        )

        DependencyContainer.addInstance(
            classType = CartRepository::class,
            instance = InMemoryCartRepository(),
            qualifier = QualifierType.IN_MEMORY,
        )
    }
}
