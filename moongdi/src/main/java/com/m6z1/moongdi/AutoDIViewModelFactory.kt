package com.m6z1.moongdi

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.m6z1.moongdi.annotation.InjectClass
import com.m6z1.moongdi.annotation.InjectField
import com.m6z1.moongdi.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

class AutoDIViewModelFactory<T : Any> : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = create(modelClass, CreationExtras.Empty)

    override fun <VM : ViewModel> create(
        modelClass: Class<VM>,
        extras: CreationExtras,
    ): VM {
        val appDependencies = extractAppDependencies(extras)
        val kClass = modelClass.kotlin

        val instance = createInstance(kClass, extras, appDependencies)

        if (kClass.hasAnnotation<InjectClass>()) {
            injectFields(instance, kClass, appDependencies)
        }

        return instance
    }

    private fun extractAppDependencies(extras: CreationExtras): T =
        (extras[APPLICATION_KEY] as? T)
            ?: throw IllegalStateException("extras에 AppDependencies가 없습니다.")

    private fun <VM : ViewModel> createInstance(
        kClass: KClass<VM>,
        extras: CreationExtras,
        appDependencies: T,
    ): VM {
        val constructor =
            kClass.primaryConstructor
                ?: return kClass.java.getDeclaredConstructor().newInstance()

        val args =
            constructor.parameters
                .associateWith { param ->
                    getDependency(param, appDependencies, extras)
                        ?: if (param.isOptional) {
                            null
                        } else {
                            throw IllegalArgumentException("주입 불가: ${param.name}")
                        }
                }.filterValues { it != null }

        return constructor.callBy(args)
    }

    private fun getDependency(
        param: KParameter,
        appDependencies: T,
        extras: CreationExtras,
    ): Any? {
        if (param.type.classifier == SavedStateHandle::class) {
            return extras.createSavedStateHandle()
        }

        return findDependencyByReflection(param, appDependencies)
    }

    private fun findDependencyByReflection(
        param: KParameter,
        appDependencies: T,
    ): Any? {
        val targetType = param.type.classifier
        val paramQualifier = extractQualifier(param.annotations)

        val dependenciesClass = appDependencies::class

        for (property in dependenciesClass.members) {
            if (property !is KProperty1<*, *>) continue
            if (property.returnType.classifier != targetType) continue

            val propQualifier = extractQualifier(property.annotations)

            if (paramQualifier == propQualifier) {
                return (property as KProperty1<T, *>).get(appDependencies)
            }
        }

        return null
    }

    private fun extractQualifier(annotations: List<Annotation>): String? =
        annotations
            .firstOrNull { annotation ->
                annotation.annotationClass.annotations.any {
                    it.annotationClass == Qualifier::class
                }
            }?.let { annotation ->
                val qualifierAnnotation =
                    annotation.annotationClass.annotations
                        .first { it.annotationClass == Qualifier::class } as Qualifier
                qualifierAnnotation.value.ifEmpty {
                    annotation.annotationClass.simpleName
                }
            }

    private fun <VM : ViewModel> injectFields(
        instance: VM,
        kClass: KClass<out ViewModel>,
        appDependencies: T,
    ) {
        kClass.declaredMemberProperties.forEach { property ->
            val field = property.javaField ?: return@forEach
            if (field.isAnnotationPresent(InjectField::class.java)) {
                val dependency =
                    getFieldDependency(property, appDependencies)
                        ?: throw IllegalArgumentException("필드 주입 불가: ${property.name}")
                field.isAccessible = true
                field.set(instance, dependency)
            }
        }
    }

    private fun getFieldDependency(
        prop: KProperty1<*, *>,
        appDependencies: T,
    ): Any? {
        val targetType = prop.returnType.classifier
        val propQualifier = extractQualifier(prop.annotations)

        val container =
            when (appDependencies) {
                is Application -> {
                    val appClass = appDependencies::class
                    val containerProp =
                        appClass.members
                            .filterIsInstance<KProperty1<*, *>>()
                            .find { it.name == "appContainer" }
                    (containerProp as? KProperty1<Any, *>)?.get(appDependencies) ?: appDependencies
                }

                else -> appDependencies
            }

        val dependenciesClass = container::class

        for (field in dependenciesClass.members) {
            if (field !is KProperty1<*, *>) continue
            if (field.returnType.classifier != targetType) continue

            val fieldQualifier = extractQualifier(field.annotations)

            if (propQualifier == fieldQualifier) {
                return (field as KProperty1<Any, *>).get(container)
            }
        }

        return null
    }
}
