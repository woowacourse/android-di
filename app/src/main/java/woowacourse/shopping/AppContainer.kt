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
    private val typeBindings: MutableMap<KClass<*>, KClass<*>> = mutableMapOf()

    init {
        providers[CartProductDao::class] = database.cartProductDao()

        typeBindings[CartRepository::class] = DefaultCartRepository::class
        typeBindings[ProductRepository::class] = DefaultProductRepository::class
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(clazz: KClass<T>): T {
        // 이미 생성된 인스턴스 반환
        providers[clazz]?.let { return it as T }

        // 2. 실제 생성할 구현체 클래스를 결정
        val implementationClass: KClass<T> =
            (typeBindings[clazz] ?: clazz) as KClass<T>

        // 3. 생성자 확인
        val constructor =
            implementationClass.primaryConstructor // implementationClass의 생성자를 찾음
                ?: throw IllegalArgumentException("${implementationClass.simpleName}은 주 생성자가 없습니다")

        // 4. 생성자 파라미터 재귀 DI
        val args =
            constructor.parameters
                .associate { param ->
                    val dependencyClass = param.type.classifier as KClass<*>
                    val dependencyInstance = get(dependencyClass) // 재귀 호출

                    param to dependencyInstance
                }

        val instance = constructor.callBy(args)

        // 5. 생성된 인스턴스 캐싱
        providers[clazz] = instance

        return instance
    }
}
