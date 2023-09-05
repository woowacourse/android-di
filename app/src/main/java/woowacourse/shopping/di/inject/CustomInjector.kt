package woowacourse.shopping.di.inject

import woowacourse.shopping.di.container.RepositoryContainer
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class CustomInjector {

    fun <T : Any> getInstanceFromClass(clazz: Class<T>): T {
        return createInstanceFromKClass(clazz.kotlin)
    }

    private fun <T : Any> createInstanceFromKClass(kClass: KClass<T>): T {
        val constructor =
            kClass.primaryConstructor ?: throw IllegalArgumentException("주 생성자를 찾을 수 없습니다.")

        val params = constructor.parameters
        val args = params.map {
            findPropertyAndGetValue(it.type)
        }.toTypedArray()

        return constructor.call(*args)
    }

    private fun findPropertyAndGetValue(type: KType): Any {
        return RepositoryContainer::class.declaredMemberProperties.find { it.returnType == type }?.let {
            it.isAccessible = true
            it.getter.call(RepositoryContainer)
        } ?: throw IllegalArgumentException("같은 타입의 프로퍼티를 찾을 수 없습니다.")
    }
}
