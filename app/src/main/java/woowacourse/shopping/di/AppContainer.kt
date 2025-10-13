package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

class AppContainer(
    private val context: Context,
) {
    // 인스턴스 캐싱
    private val instances = mutableMapOf<KClass<*>, Any>()

    private val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            "shopping-database"
        ).build()
    }

    fun <T : Any> getInstance(clazz: KClass<T>): T {
        // 이미 있는 인스턴스면 반환
        instances[clazz]?.let { return it as T }

        // RoomDB의 Dao는 따로 정의
        val instance: Any = when (clazz) {
            CartProductDao::class -> shoppingDatabase.cartProductDao()
            else -> createInstance(clazz)
        }

        // 없으면 인스턴스 생성
        instances[clazz] = instance
        return instance as T
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor: KFunction<T> =
            clazz.primaryConstructor ?: throw IllegalStateException("주생성자를 찾을 수 없습니다.")
        val args: List<Any> = constructor.parameters.map { param ->
            val paramClass = param.type.classifier as? KClass<*>
                ?: throw IllegalStateException("유효하지 않은 파라미터 타입입니다.")
            getInstance(paramClass) // 재귀적으로 의존성 생성
        }
        return constructor.call(*args.toTypedArray())
    }
}
