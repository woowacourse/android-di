package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass

class ShoppingContainer(
    context: Context,
) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    private val database =
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()

    fun getInstance(targetClass: KClass<*>): Any? = instances[targetClass]

    fun saveInstance(
        key: KClass<*>,
        value: Any,
    ) {
        instances[key] = value
    }
}
