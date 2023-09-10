package woowacourse.shopping.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainer {
    private val providers: MutableMap<KClass<*>, KFunction<*>> = mutableMapOf()

    private val repositories: MutableMap<KClass<*>, Any> by lazy {
        mutableMapOf(
            ProductRepository::class to DefaultProductRepository(),
            CartRepository::class to DefaultCartRepository(providers[CartProductDao::class]?.call() as CartProductDao),
        )
    }

    fun <T : Any> addProvider(clazz: KClass<T>, provider: KFunction<T>) {
        providers[clazz] = provider
    }

    fun <T : Any> inject(clazz: Class<T>): T {
        val constructor = clazz.kotlin.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")
        val args = constructor.parameters
            .associateWith { repositories[it.type.jvmErasure] }
            .filterValues { it != null }
        return constructor.callBy(args)
    }
}
