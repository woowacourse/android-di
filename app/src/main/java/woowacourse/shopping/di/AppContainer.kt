package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class AppContainer {
    private val productRepository = DefaultProductRepository()
    private val cartRepository = DefaultCartRepository()

    fun <T : Any> inject(clazz: Class<T>): T {
        val constructor = clazz.kotlin.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")
        val args = constructor.parameters.associateWith { parameter ->
            this::class.declaredMemberProperties
                .find { it.returnType == parameter.type }
                ?.let {
                    it.isAccessible = true
                    it.getter.call(this)
                } ?: throw NoSuchElementException("생성자 주입을 위한 프로퍼티의 인스턴스가 존재하지 않습니다.")
        }

        return constructor.callBy(args)
    }
}
