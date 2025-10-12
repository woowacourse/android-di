package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AppContainer(
    private val context: Context,
) {
    private val database: ShoppingDatabase by lazy {
        Room
            .databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                "shopping-database",
            ).build()
    }
    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        providers[CartProductDao::class] = database.cartProductDao()
        providers[CartRepository::class] = DefaultCartRepository(get(CartProductDao::class))
        providers[ProductRepository::class] = DefaultProductRepository()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(clazz: KClass<T>): T {
        // 이미 생성된 인스턴스 반환
        providers[clazz]?.let { return it as T }

        // 생성자 확인
        val constructor =
            clazz.primaryConstructor
                ?: throw IllegalArgumentException("${clazz.simpleName}은 주 생성자가 없습니다")

        // 생성자 파라미터 재귀 DI
        val args =
            constructor.parameters
                .associateWith { param ->
                    val dependencyClass = param.type.classifier as KClass<*>
                    param to get(dependencyClass) // 재귀 호출
                }.toMap()

        val instance = constructor.callBy(args)
        providers[clazz] = instance

        return instance
    }
}
