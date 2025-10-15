package com.on.di_library.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.on.di_library.di.annotation.MyInjector
import com.on.di_library.di.annotation.MyModule
import com.on.di_library.di.annotation.MyProvider
import com.on.di_library.di.annotation.MyQualifier
import dalvik.system.DexFile
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DiContainer {
    private val instancePool: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()

    private val functionWithQualifier = mutableMapOf<KClass<*>, MutableList<DependencyKey>>()

    data class DependencyKey(
        val module: KClass<*>,
        val function: KFunction<*>,
        val qualifier: String?,
    )

    /** DiContainer에서 Context를 사용할 수 있도록 설정 **/
    fun setContext(context: Context) {
        instancePool[Context::class] = context
    }

    /** 현재 패키지에 있는 클래스들 중 MyModule의 어노테이션이 붙어있는 모듈 찾기**/
    fun getAnnotatedModules() {
        val context = instancePool[Context::class] as Application
        val dexFile = DexFile(context.packageCodePath)
        val entries = dexFile.entries()

        val annotatedClass = buildList {
            while (entries.hasMoreElements()) {
                val className = entries.nextElement()

                runCatching {
                    val clazz = Class.forName(className).kotlin

                    if (clazz.hasAnnotation<MyModule>()) {
                        add(clazz)
                    }
                }
            }
        }
        createModuleFunction(annotatedClass)
    }

    /**
     * MyModule 어노테이션이 붙어있는 모듈들 중 MyProvider가 붙어있는 함수를 불러옴
     * 하나의 인터페이스의 여러 구현체가 있는 경우 MyQualifier를 통해 list로 해당 함수를 저장
     * 함수를 실행하는 것이 아닌 KFunction을 저장하는 것
     * **/
    private fun createModuleFunction(modulePool: List<KClass<*>>) {
        modulePool.forEach { module ->
            module.declaredMemberFunctions.forEach { function ->
                if (!function.hasAnnotation<MyProvider>()) return@forEach

                val returnTypeKClass =
                    function.returnType.classifier as? KClass<*> ?: return@forEach
                val qualifier = function.findAnnotation<MyQualifier>()?.type

                if (!functionWithQualifier.containsKey(returnTypeKClass))
                    functionWithQualifier[returnTypeKClass] = mutableListOf()

                functionWithQualifier[returnTypeKClass]?.add(
                    DependencyKey(module, function, qualifier)
                )
            }
        }
    }

    /** 외부에 인스턴스를 주입해주는 주는 메서드**/
    fun <T : Any> getInstance(kClass: KClass<T>, qualifier: String? = null): T {
        if (ViewModel::class.java.isAssignableFrom(kClass.java)) {
            return createInstance(kClass)
        }

        instancePool[kClass]?.let {
            return kClass.cast(it)
        }

        val newInstance = createInstance(kClass, qualifier)
        instancePool[kClass] = newInstance

        return kClass.cast(newInstance)
    }

    /** MyInjector어노테이션이 붙어 있는 프로퍼티를 찾아 인스턴스 주입 **/
    private fun <T : Any> injectFieldProperties(
        implementClass: KClass<out Any>,
        instance: T,
    ) {
        implementClass.declaredMemberProperties
            .filter { it.hasAnnotation<MyInjector>() }
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .forEach { property ->
                property.isAccessible = true
                val propertyInstance =
                    getInstance(
                        property.returnType.classifier as? KClass<*>
                            ?: error(""),
                        property.findAnnotation<MyQualifier>()?.type
                    )
                property.setter.call(instance, propertyInstance)
            }
    }

    /**
     * 요청된 타입에 맞는 인스턴스를 생성하는 메서드
     *  먼저 MyProvider가 있는 모듈 함수를 통해 생성 가능한지 확인
     *  없을 경우 기본 생성자를 통해 직접 인스턴스를 생성
     **/
    private fun <T : Any> createInstance(kClass: KClass<T>, qualifier: String? = null): T {
        val possibleProviders: List<DependencyKey> = functionWithQualifier[kClass] ?: emptyList()

        val matchedProvider: DependencyKey? = possibleProviders.find { it.qualifier == qualifier }

        matchedProvider?.let { key ->
            return createFromModule(key.function, kClass, key.module)
        }

        val instance: T = createFromConstructor(kClass)
        injectFieldProperties(kClass, instance)
        return instance
    }

    /**
     * MyModule 내의 MyProvider 함수로부터 인스턴스를 생성하는 메서드
     * 함수의 파라미터를 재귀를 통해 주입받고
     * 모듈 함수 호출을 통해 객체를 반환
     **/
    private fun <T : Any> createFromModule(
        function: KFunction<*>,
        kClass: KClass<T>,
        module: KClass<*>,
    ): T {
        val constructorArguments: Map<KParameter, Any> =
            function.parameters.associateWith { parameter ->
                if (parameter.kind == KParameter.Kind.INSTANCE) {
                    module.objectInstance
                        ?: error(ERROR_MESSAGE_NOT_OBJECT.format(module.simpleName))
                } else {
                    val parameterClass: KClass<*> =
                        parameter.type.classifier as? KClass<*>
                            ?: error("")
                    getInstance(parameterClass)
                }
            }

        val instance = kClass.cast(function.callBy(constructorArguments))
        injectFieldProperties(kClass, instance)

        return instance
    }

    /**
     * 클래스의 기본 생성자를 이용해 인스턴스를 생성하는 메서드
     * 생성자 파라미터에 필요한 의존성을 재귀로 주입
     **/
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

        val instance: T = kClass.cast(constructor.callBy(arguments))

        injectFieldProperties(kClass, instance)

        return instance
    }

    private const val ERROR_MESSAGE_NOT_OBJECT = "%s은 object가 아닙니다."
    private const val ERROR_MESSAGE_NOT_HAVE_DEFAULT_CONSTRUCTOR = "%s 클래스에 기본 생성자가 없습니다."
    private const val ERROR_MESSAGE_CANNOT_GET_INSTANCE = "생성자 %s의 파라미터 %s 타입을 가져올 수 없습니다."
}