package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.ViewModel
import dalvik.system.DexFile
import woowacourse.shopping.di.annotation.MyInjector
import woowacourse.shopping.di.annotation.MyModule
import woowacourse.shopping.di.annotation.MyProvider
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
    private lateinit var modulePool: List<KClass<*>>

    fun getAnnotatedModules(context: Context) {
        val result = mutableListOf<KClass<*>>()

        val dexFile = DexFile(context.packageCodePath)
        val entries = dexFile.entries()

        while (entries.hasMoreElements()) {
            val className = entries.nextElement()

            runCatching {
                val clazz = Class.forName(className).kotlin

                if (clazz.hasAnnotation<MyModule>()) {
                    result.add(clazz)
                }
            }
        }

        modulePool = result
    }

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
            .forEach { property ->
                val mutableProp: KMutableProperty1<T, Any> = property as KMutableProperty1<T, Any>
                mutableProp.isAccessible = true
                val propertyInstance =
                    getInstance(
                        property.returnType.classifier as? KClass<*>
                            ?: error(""),
                    )
                mutableProp.setter.call(instance, propertyInstance)
            }
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        modulePool.forEach { module ->
            module.declaredMemberFunctions.forEach { function ->
                if (!function.hasAnnotation<MyProvider>()) return@forEach
                val returnTypeKClass = function.returnType.classifier as? KClass<*>
                if (returnTypeKClass == kClass || returnTypeKClass != null && kClass in returnTypeKClass.supertypes.map { it.classifier }) {
                    return createFromModule(function, kClass, module)
                }
            }
        }

        val instance = createFromConstructor(kClass)
        injectFieldProperties(kClass, instance)

        return instance
    }

    private fun <T : Any> createFromModule(
        function: KFunction<*>,
        kClass: KClass<T>,
        module: KClass<*>,
    ): T {
        val constructorArguments =
            function.parameters.associateWith { parameter ->
                if (parameter.kind == KParameter.Kind.INSTANCE) {
                    module.objectInstance
                        ?: error(ERROR_MESSAGE_NOT_OBJECT.format(module.simpleName))
                } else {
                    val parameterClass =
                        parameter.type.classifier as? KClass<*>
                            ?: error("")
                    getInstance(parameterClass)
                }
            }

        val instance = kClass.cast(function.callBy(constructorArguments))
        injectFieldProperties(kClass, instance)

        return instance
    }

    private fun <T : Any> createFromConstructor(kClass: KClass<T>): T {
        val constructor: KFunction<Any> =
            kClass.primaryConstructor
                ?: error(ERROR_MESSAGE_NOT_HAVE_DEFAULT_CONSTRUCTOR.format(kClass.simpleName))

        val arguments: Map<KParameter, Any> =
            constructor.parameters.associateWith { parameter ->
                getInstance(
                    parameter.type.classifier as? KClass<*>
                        ?: error(
                            ERROR_MESSAGE_CANNOT_GET_INSTANCE.format(
                                kClass.simpleName,
                                parameter,
                            ),
                        ),
                )
            }

        val instance = kClass.cast(constructor.callBy(arguments))

        injectFieldProperties(kClass, instance)

        return instance
    }

    private const val ERROR_MESSAGE_NOT_OBJECT = "%s은 object가 아닙니다."
    private const val ERROR_MESSAGE_NOT_HAVE_DEFAULT_CONSTRUCTOR = "%s 클래스에 기본 생성자가 없습니다."
    private const val ERROR_MESSAGE_CANNOT_GET_INSTANCE = "생성자 %s의 파라미터 %s 타입을 가져올 수 없습니다."
}
