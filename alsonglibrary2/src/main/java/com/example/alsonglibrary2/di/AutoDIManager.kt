package com.example.alsonglibrary2.di

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
    val dependencies: MutableMap<KClass<*>, Any?> = mutableMapOf()

    var provider: LibraryDependencyProvider? = null

    inline fun <reified T : Any> registerDependency(dependency: Any) {
        dependencies[T::class] = dependency
    }

    inline fun <reified VM : ViewModel> createViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                createAutoDIInstance<VM>()
            }
        }
    }

    inline fun <reified T : Any> createAutoDIInstance(): T {
//        addQualifierDependency<T>()

        // constructor 주입
        val clazz = T::class
        val constructor = clazz.primaryConstructor ?: return clazz.createInstance()
        val args = constructor.parameters.associateWith { dependencies[it.type.jvmErasure] }.toMutableMap()

        // 어노테이션대로 바꾸기
        val parametersWithAnnotation = constructor.parameters.filter { it.annotations.isNotEmpty() }
        for (parameter in parametersWithAnnotation) {
            // 필드에도 적용할 수 있도록 수정 필요
            val annotation = parameter.annotations.first()
            args[parameter] = fetchAnnotationParamsValue(annotation) ?: continue
        }
        val instance = constructor.callBy(args)

        // 필드 주입
        val fieldInjectedInstance = injectField<T>(instance)

        return fieldInjectedInstance
    }

    inline fun <reified T : Any> addQualifierDependency() {
        val kProperties = T::class.declaredMemberProperties.filter { it.annotations.isNotEmpty() }
        for (kProperty in kProperties) {
            // 필드에도 적용할 수 있도록 수정 필요
            val annotation = kProperty.annotations.filterNot { it is FieldInject}.first()
            Log.d("alsong", "addQualifierDependency: ${annotation}")
            dependencies[kProperty.returnType.jvmErasure] = fetchAnnotationParamsValue(annotation) ?: continue
        }

        val constructor = T::class.primaryConstructor ?: return
        val parametersWithAnnotation = constructor.parameters.filter { it.annotations.isNotEmpty() }
        for (parameter in parametersWithAnnotation) {
            // 필드에도 적용할 수 있도록 수정 필요
            val annotation = parameter.annotations.first()
            dependencies[parameter.type.jvmErasure] = fetchAnnotationParamsValue(annotation) ?: continue
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
     * Qualifier 어노테이션이 붙은 함수를 DependencyProvider에서 찾아서 호출합니다.
     **/
    inline fun <reified A : Annotation> fetchAnnotationParamsValue(annotation: A): Any? {
        val dependencyProvider = provider ?: return null
        val targetFunction = dependencyProvider::class.memberFunctions
            .find { it.findAnnotation<A>() == annotation } ?: return null
        return targetFunction.call(dependencyProvider)
    }
}
