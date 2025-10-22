package com.m6z1.moongdi

import com.m6z1.moongdi.annotation.ActivityScope
import com.m6z1.moongdi.annotation.ApplicationScope
import com.m6z1.moongdi.annotation.Qualifier
import com.m6z1.moongdi.annotation.Single
import com.m6z1.moongdi.annotation.ViewModelScope
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object ScopedDependencyContainer {
    private val applicationSingletons: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val activityScopes: MutableMap<String, MutableMap<KClass<*>, Any>> = mutableMapOf()
    private val viewModelScopes: MutableMap<String, MutableMap<KClass<*>, Any>> = mutableMapOf()
    private val qualifierDependencies: MutableMap<KClass<out Annotation>, Any> = mutableMapOf()

    private data class QualifiedKey(
        val qualifier: KClass<out Annotation>,
        val type: KClass<*>,
    )

    private val qualifiedInterfaces: MutableMap<QualifiedKey, Any> = mutableMapOf()

    fun register(instance: Any) {
        val clazz = instance::class
        val scope = getScopeFromClass(clazz)

        when (scope) {
            Scope.APPLICATION -> registerToApplication(clazz, instance)
            else -> registerToApplication(clazz, instance)
        }
    }

    fun registerToApplication(
        clazz: KClass<*>,
        instance: Any,
    ) {
        val qualifierAnnotation =
            clazz.annotations.firstOrNull { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }

        if (qualifierAnnotation != null) {
            val qualifierClass = qualifierAnnotation.annotationClass

            qualifierDependencies[qualifierClass] = instance

            clazz.supertypes.forEach { superType ->
                val superClass = superType.classifier as? KClass<*>
                if (superClass != null &&
                    superClass != Any::class &&
                    (superClass.isAbstract || superClass.java.isInterface)
                ) {
                    val key = QualifiedKey(qualifierClass, superClass)
                    qualifiedInterfaces[key] = instance
                }
            }
        } else {
            applicationSingletons[clazz] = instance
            registerSuperTypes(clazz, instance, applicationSingletons)
        }
    }

    fun registerToActivity(
        activityId: String,
        clazz: KClass<*>,
        instance: Any,
    ) {
        val scopeMap = activityScopes.getOrPut(activityId) { mutableMapOf() }
        scopeMap[clazz] = instance
        registerSuperTypes(clazz, instance, scopeMap)
    }

    fun registerToViewModel(
        viewModelId: String,
        clazz: KClass<*>,
        instance: Any,
    ) {
        val scopeMap = viewModelScopes.getOrPut(viewModelId) { mutableMapOf() }
        scopeMap[clazz] = instance
        registerSuperTypes(clazz, instance, scopeMap)
    }

    private fun registerSuperTypes(
        clazz: KClass<*>,
        instance: Any,
        targetMap: MutableMap<KClass<*>, Any>,
    ) {
        clazz.supertypes.forEach { superType ->
            val superClass = superType.classifier as? KClass<*>
            if (superClass != null &&
                superClass != Any::class &&
                (superClass.isAbstract || superClass.java.isInterface)
            ) {
                targetMap[superClass] = instance
            }
        }
    }

    fun provide(
        clazz: Class<*>,
        activityId: String? = null,
        viewModelId: String? = null,
        qualifier: KClass<out Annotation>? = null,
    ): Any {
        val kClazz: KClass<*> = clazz.kotlin

        if (qualifier != null) {
            val key = QualifiedKey(qualifier, kClazz)
            qualifiedInterfaces[key]?.let { return it }

            return qualifierDependencies[qualifier]
                ?: throw IllegalStateException("@${qualifier.simpleName}가 붙은 ${kClazz.simpleName} 의존성이 등록되지 않았습니다.")
        }

        if (viewModelId != null) {
            viewModelScopes[viewModelId]?.get(kClazz)?.let { return it }
        }
        if (activityId != null) {
            activityScopes[activityId]?.get(kClazz)?.let { return it }
        }
        applicationSingletons[kClazz]?.let { return it }

        throw IllegalStateException("$kClazz 가 등록되지 않았습니다.")
    }

    fun clearActivityScope(activityId: String) {
        activityScopes.remove(activityId)
    }

    fun clearViewModelScope(viewModelId: String) {
        viewModelScopes.remove(viewModelId)
    }

    private fun getScopeFromClass(clazz: KClass<*>): Scope =
        when {
            clazz.hasAnnotation<ApplicationScope>() -> Scope.APPLICATION
            clazz.hasAnnotation<ActivityScope>() -> Scope.ACTIVITY
            clazz.hasAnnotation<ViewModelScope>() -> Scope.VIEWMODEL
            clazz.hasAnnotation<Single>() -> Scope.APPLICATION
            else -> Scope.APPLICATION
        }
}
