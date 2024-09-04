package woowacourse.shopping.ui.util

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AppModule
import woowacourse.shopping.ui.ShoppingApplication

class Injector(
    private val appModule: AppModule = ShoppingApplication.appModule,
) {
    fun inject(activity: AppCompatActivity) {
        val properties = activity::class.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { it.isLateinit }

        properties.forEach { property ->
            injectProperty(property, activity)
        }
    }

    private fun injectProperty(
        property: KMutableProperty<*>,
        activity: AppCompatActivity
    ) {
        property.isAccessible = true
        findValueForProperty(property, appModule)?.let { value ->
            property.setter.call(activity, value)
        }
    }

    private fun findValueForProperty(property: KMutableProperty<*>, appModule: AppModule): Any? {
        return when (property.returnType) {
            typeOf<CartRepository>() -> appModule.cartRepository
            typeOf<ProductRepository>() -> appModule.productRepository
            else -> null
        }
    }
}
