package woowacourse.shopping.di.inject

import woowacourse.shopping.di.container.RepositoryContainer
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class CustomInject {

    fun <T : Any> getInstance(clazz: Class<T>): T {
        return createInstance(clazz.kotlin)
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructors =
            kClass.primaryConstructor ?: throw IllegalArgumentException("주 생성자를 찾을 수 없습니다.")

        val params = constructors.parameters
        val arg = params.map {
            findPropertyAndGetValue(it.type)
        }.toTypedArray()

        return constructors.call(*arg)
    }

    private fun findPropertyAndGetValue(type: KType): Any {
        return RepositoryContainer::class.declaredMemberProperties.find { it.returnType == type }?.let {
            it.isAccessible = true
            it.getter.call(RepositoryContainer)
        } ?: throw IllegalArgumentException("같은 타입의 프로퍼티를 찾을 수 없습니다.")
    }
}
