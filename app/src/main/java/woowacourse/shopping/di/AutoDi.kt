package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoDi(
    private val container: ShoppingContainer,
) {
    fun <T : Any> createInstance(targetClass: KClass<T>): T {
        val constructor =
            targetClass.primaryConstructor
                ?: throw IllegalArgumentException("${targetClass.simpleName}의 주 생성자가 없습니다.")

        val dependencyClasses =
            constructor.parameters.map { param ->
                val dependencyClass =
                    param.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${param.name}은 클래스가 아닙니다.")

                getInstance(dependencyClass)
            }

        return constructor.call(*dependencyClasses.toTypedArray())
    }

    private fun getInstance(targetClass: KClass<*>): Any {
        container.getInstance(targetClass)?.let {
            return it
        }

        val instance = createInstance(targetClass)
        container.saveInstance(targetClass, instance)

        return instance
    }
}
