package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class DependencyFactory(private val container: Container) {
    fun <T : Any> getTarget(target: KClass<T>): T {
        val primaryConstructor = target.primaryConstructor
            ?: throw IllegalStateException("주 생성자가 없습니다.")
        val params = primaryConstructor.parameters.instances
        return primaryConstructor.call(*params.toTypedArray())
    }

    private val List<KParameter>.instances get() = map { getParam(it.type) }

    private fun getParam(paramType: KType): Any {
        val dependency = container::class.declaredMemberProperties
            .find { paramType == it.returnType }
            ?: throw IllegalStateException("적합한 의존성이 없습니다")

        return dependency.getter.call(container)
            ?: throw IllegalStateException("적합한 의존성이 없습니다")
    }
}
