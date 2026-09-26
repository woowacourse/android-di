package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

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

        val instance = constructor.call(*dependencyClasses.toTypedArray())

        injectProperties(targetClass, instance)

        return instance
    }

    private fun injectProperties(
        targetClass: KClass<*>,
        instance: Any,
    ) {
        targetClass.memberProperties
            .filter { it.findAnnotation<InjectProperty>() != null }
            .forEach { property ->
                val mutableProperty =
                    property as? KMutableProperty1<*, *>
                        ?: throw IllegalArgumentException(
                            "${targetClass.simpleName}.${property.name}은 variable이어야 합니다.",
                        )

                val dependencyClass =
                    property.returnType.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${property.name}은 클래스가 아닙니다.")

                mutableProperty.isAccessible = true
                mutableProperty.setter.call(instance, getInstance(dependencyClass))
            }
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
