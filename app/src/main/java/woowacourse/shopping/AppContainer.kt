package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Database
import woowacourse.shopping.di.InMemory
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
    private val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, Any>()
    private val typeBindings = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, KClass<*>>()

    init {
        providers[CartProductDao::class to null] = database.cartProductDao()

        typeBindings[CartRepository::class to Database::class] = DefaultCartRepository::class
        typeBindings[ProductRepository::class to Database::class] = DefaultProductRepository::class

        typeBindings[ProductRepository::class to InMemory::class] = DefaultProductRepository::class
        typeBindings[CartRepository::class to InMemory::class] = DefaultCartRepository::class

        typeBindings[ProductRepository::class to null] = DefaultProductRepository::class
        typeBindings[CartRepository::class to null] = DefaultCartRepository::class
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = clazz to qualifier

        // 이미 생성된 인스턴스 있으면 반환
        providers[key]?.let { return it as T }

        // 실제 구현체 클래스 찾기
        val implementationClass =
            (typeBindings[key] ?: typeBindings[clazz to null] ?: clazz) as KClass<T>

        // 주 생성자 확인
        val primaryConstructor = implementationClass.primaryConstructor

        val instance: T =
            if (primaryConstructor != null) {
                // 생성자 기반 의존성 주입
                val constructorArgs =
                    primaryConstructor.parameters.associate { param ->
                        val depClass = param.type.classifier as KClass<*>
                        val depQualifier =
                            param.annotations.firstOrNull()?.annotationClass as? KClass<out Annotation>
                        val dep = get(depClass, depQualifier)
                        param to dep
                    }
                primaryConstructor.callBy(constructorArgs)
            } else {
                // 기본 생성자 존재 시 자동 인스턴스화
                try {
                    implementationClass.java.getDeclaredConstructor().newInstance()
                } catch (e: Exception) {
                    throw IllegalArgumentException(
                        "${implementationClass.simpleName}을 생성할 수 없습니다: 기본 생성자 없음",
                        e,
                    )
                }
            }

        // 생성된 인스턴스 캐싱
        providers[key] = instance

        return instance
    }
}
