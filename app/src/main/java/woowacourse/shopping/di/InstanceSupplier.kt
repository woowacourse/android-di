package woowacourse.shopping.di

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.kotlinProperty
import woowacourse.shopping.ui.ShoppingApplication

object InstanceSupplier {
    private const val EXCEPTION_PROPERTY_NOT_FOUND = "Property that needs to be injected not found"
    private const val EXCEPTION_NO_TARGET_CONSTRUCTOR =
        "No constructor with @Supply annotation found for %s"

    fun <T : Any> injectFields(
        clazz: Class<T>,
        instance: Any,
    ) {
        val targetFields = clazz.declaredFields.filter { field ->
            field.annotations.any { annotation ->
                annotation.annotationClass == Supply::class
            }
        }

        targetFields.forEach { targetField ->
            injectSingleField(targetField, instance)
        }
    }

    private fun injectSingleField(property: Field, instance: Any) {
        property.isAccessible = true
        property.set(
            instance,
            ShoppingApplication.instanceContainer.instanceOf(
                property.kotlinProperty?.returnType ?: error(EXCEPTION_PROPERTY_NOT_FOUND)
            )
        )
    }

    // TODO 3단계 구현 시 생성자 주입 + 필드 주입 혼합하여 사용할 수 있도록 수정
    fun <T : Any> injectedInstance(
        clazz: Class<T>,
    ): T {
        val targetConstructor = targetConstructor(clazz)
        val constructorParameters = targetConstructor.parameters
        val parameterValues =
            constructorParameters.map { parameter ->
                val parameterClass = parameter.type.kotlin
                val parameterType = parameterClass.createType()
                ShoppingApplication.instanceContainer.instanceOf<T>(parameterType)
            }.toTypedArray<Any>()

        val instance = targetConstructor.newInstance(*parameterValues)
        injectFields(clazz, instance)

        return instance as T
    }

    // TODO 3단계 구현 시 생성자 주입 + 필드 주입 혼합하여 사용할 수 있도록 수정
    private fun <T : Any> targetConstructor(clazz: Class<T>): Constructor<*> {
        val constructors = clazz.constructors
        val targetConstructor = constructors.firstOrNull { constructor ->
            constructor.annotations.any { annotation ->
                annotation.annotationClass == Supply::class
            }
        }

        return checkNotNull(targetConstructor) {
            EXCEPTION_NO_TARGET_CONSTRUCTOR.format(clazz.simpleName)
        }
    }
}
