package com.example.alsonglibrary2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ui.util.DependencyProvider
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object AutoDIManager {
    val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    inline fun <reified T : Any> registerDependency(dependency: Any) {
        val clazz = T::class
        dependencies[clazz] = dependency
    }

    inline fun <reified VM : ViewModel> createViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                createAutoDIInstance<VM>()
            }
        }
    }

    inline fun <reified T : Any> injectField(instance: T): T {
        val properties = T::class.declaredMemberProperties
        val mutableProperties = properties.filterIsInstance<KMutableProperty<*>>()
        for (mutableProperty in mutableProperties) {
            if (mutableProperty.findAnnotation<FieldInject>() != null) {
                mutableProperty.isAccessible = true
                mutableProperty.setter.call(
                    instance,
                    dependencies[mutableProperty.returnType.jvmErasure],
                )
            }
        }
        return instance
    }

    /**
     * Qualifier 어노테이션을 무시하고 Application에서 등록된 인스턴스를 주입합니다.
     **/
    inline fun <reified T : Any> createNoQualifierInstance(): T {
        val clazz = T::class
        val constructor = clazz.primaryConstructor ?: return clazz.createInstance()

        val args: MutableMap<KParameter, Any?> = mutableMapOf()
        constructor.parameters.forEach { kParameter ->
            args[kParameter] = dependencies[kParameter.type.jvmErasure]
        }
        val instance = constructor.callBy(args)
        return injectField<T>(instance)
    }

    /**
     * createNoQualifierInstance()가 리턴한 인스턴스에서 Qualifier가 붙은 생성자 파라미터의 값을
     * DependencyProvider에서 설정한 인스턴스로 변경합니다.
     **/
    inline fun <reified T : Any> createAutoDIInstance(): T {
        val instance = createNoQualifierInstance<T>()
        val constructor = instance::class.primaryConstructor ?: return instance
        for (kParameter in constructor.parameters) {
            if (kParameter.annotations.isNotEmpty()) {
                val annotation = kParameter.annotations.first()
                val kParameterName = kParameter.name ?: return instance
                val parameter = T::class.java.getDeclaredField(kParameterName)
                parameter.apply {
                    isAccessible = true
                    set(instance, fetchAnnotationParamsValue(annotation))
                }
                continue
            }
        }
        return instance
    }

    /**
     * Qualifier 어노테이션이 붙은 함수를 DependencyProvider에서 찾아서 호출합니다.
     **/
    inline fun <reified A : Annotation> fetchAnnotationParamsValue(annotation: A): Any? {
        return DependencyProvider::class.memberFunctions
            .first { it.findAnnotation<A>() != null }
            .call(DependencyProvider)
    }
}
