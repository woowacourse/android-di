package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DiContainer {
    private val instancePool: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()

    fun <T : Any> getInstance(kClass: KClass<T>): T {
        if (ViewModel::class.java.isAssignableFrom(kClass.java)) {
            return createInstance(kClass)
        }

        instancePool[kClass]?.let {
            return kClass.cast(it)
        }

        val newInstance = createInstance(kClass)
        instancePool[kClass] = newInstance

        return kClass.cast(newInstance)
    }

    private fun <T : Any> injectFieldProperties(
        implementClass: KClass<out Any>,
        instance: T,
    ) {
        implementClass.declaredMemberProperties
            .filter { it.hasAnnotation<MyInjector>() }
            .forEach { prop ->
                val mutableProp: KMutableProperty1<T, Any> = prop as KMutableProperty1<T, Any>
                mutableProp.isAccessible = true
                val propInstance = getInstance(
                    prop.returnType.classifier as? KClass<*>
                        ?: error("")
                )
                mutableProp.setter.call(instance, propInstance)
            }
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val module = RepositoryModule::class

        module.declaredMemberFunctions.forEach { function ->
            val returnTypeKClass = function.returnType.classifier as? KClass<*>
            if (returnTypeKClass == kClass || returnTypeKClass != null && kClass in returnTypeKClass.supertypes.map { it.classifier }) {
                return createFromModule(function, kClass)
            }
        }

        val instance = createFromConstructor(kClass)
        injectFieldProperties(kClass, instance)

        return instance
    }

    private fun <T : Any> createFromModule(
        function: KFunction<*>,
        kClass: KClass<T>,
    ): T {
        val constructorArguments = function.parameters.associateWith { parameter ->
            if (parameter.kind == KParameter.Kind.INSTANCE) {
                RepositoryModule
            } else {
                val parameterClass = parameter.type.classifier as? KClass<*>
                    ?: error("")
                getInstance(parameterClass)
            }
        }

        val instance = kClass.cast(function.callBy(constructorArguments))
        injectFieldProperties(kClass, instance)
        return instance
    }

    private fun <T : Any> createFromConstructor(kClass: KClass<T>): T {
        val constructor: KFunction<Any> = kClass.primaryConstructor
            ?: error(ERROR_MESSAGE_NOT_HAVE_DEFAULT_CONSTRUCTOR.format(kClass.simpleName))

        val arguments: Map<KParameter, Any> = constructor.parameters.associateWith { param ->
            getInstance(
                param.type.classifier as? KClass<*>
                    ?: error(
                        ERROR_MESSAGE_CANNOT_GET_INSTANCE.format(
                            kClass.simpleName,
                            param
                        )
                    )
            )
        }

        val instance = kClass.cast(constructor.callBy(arguments))

        injectFieldProperties(kClass, instance)

        return instance
    }

    private const val ERROR_MESSAGE_NOT_HAVE_DEFAULT_CONSTRUCTOR = "%s 클래스에 기본 생성자가 없습니다."
    private const val ERROR_MESSAGE_CANNOT_GET_INSTANCE = "생성자 %s의 파라미터 %s 타입을 가져올 수 없습니다."
}
