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

                getInstance(dependencyClass, findQualifier(param.annotations))
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
                mutableProperty.setter.call(instance, getInstance(dependencyClass, findQualifier(property.annotations)))
            }
    }

    private fun findQualifier(annotations: List<Annotation>): KClass<out Annotation>? {
        val qualifiers = annotations.map { it.annotationClass }.filter { it.findAnnotation<Qualifier>() != null }

        require(qualifiers.size <= 1) {
            "Qualifier은 하나만 지정할 수 있습니다."
        }

        return qualifiers.singleOrNull()
    }

    private fun getInstance(
        targetClass: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any {
        container.getInstance(targetClass, qualifier)?.let {
            return it
        }

        require(qualifier == null) {
            "${targetClass.simpleName}에 ${qualifier?.simpleName} Qualifier로 등록된 의존성이 없습니다."
        }

        val instance = createInstance(targetClass)
        container.saveInstance(targetClass, instance)

        return instance
    }
}
