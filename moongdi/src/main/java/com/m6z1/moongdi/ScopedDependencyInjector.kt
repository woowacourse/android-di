package com.m6z1.moongdi

import com.m6z1.moongdi.annotation.ActivityScope
import com.m6z1.moongdi.annotation.ApplicationScope
import com.m6z1.moongdi.annotation.InjectClass
import com.m6z1.moongdi.annotation.InjectField
import com.m6z1.moongdi.annotation.Qualifier
import com.m6z1.moongdi.annotation.Single
import com.m6z1.moongdi.annotation.ViewModelScope
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.jvmErasure

object ScopedDependencyInjector {
    data class InjectionContext(
        val activityId: String? = null,
        val viewModelId: String? = null,
    )

    private val threadLocalContext = ThreadLocal<InjectionContext>()

    fun setContext(context: InjectionContext) {
        threadLocalContext.set(context)
    }

    fun clearContext() {
        threadLocalContext.remove()
    }

    fun getContext(): InjectionContext = threadLocalContext.get() ?: InjectionContext()

    inline fun <reified T : Any> inject(): T = inject(T::class) as T

    fun inject(clazz: KClass<*>): Any {
        val context = getContext()
        val scope = getScopeFromClass(clazz)

        val cached =
            when (scope) {
                Scope.APPLICATION -> {
                    try {
                        ScopedDependencyContainer.provide(
                            clazz.java,
                            activityId = null,
                            viewModelId = null,
                        )
                    } catch (e: IllegalStateException) {
                        null
                    }
                }

                Scope.ACTIVITY -> {
                    context.activityId?.let { activityId ->
                        try {
                            ScopedDependencyContainer.provide(
                                clazz.java,
                                activityId = activityId,
                                viewModelId = null,
                            )
                        } catch (e: IllegalStateException) {
                            null
                        }
                    }
                }

                Scope.VIEWMODEL -> {
                    context.viewModelId?.let { viewModelId ->
                        try {
                            ScopedDependencyContainer.provide(
                                clazz.java,
                                activityId = context.activityId,
                                viewModelId = viewModelId,
                            )
                        } catch (e: IllegalStateException) {
                            null
                        }
                    }
                }
            }

        cached?.let { return it }

        val constructor =
            clazz.primaryConstructor
                ?: clazz.constructors.maxByOrNull { it.parameters.size }
                ?: throw IllegalStateException("생성자를 찾을 수 없음: ${clazz.simpleName}")

        val instance =
            if (clazz.hasAnnotation<InjectClass>()) {
                val args: Map<KParameter, Any?> =
                    constructor.parameters.associateWith { param ->
                        val paramClass = param.type.jvmErasure

                        if (paramClass.isAbstract || paramClass.java.isInterface) {
                            ScopedDependencyContainer.provide(
                                paramClass.java,
                                context.activityId,
                                context.viewModelId,
                            )
                        } else {
                            paramClass.instantiate(context)
                        }
                    }

                constructor.callBy(args).apply {
                    injectField(this, context)
                }
            } else {
                val createdInstance =
                    if (constructor.parameters.isEmpty()) {
                        constructor.call()
                    } else {
                        val args =
                            constructor.parameters.associateWith { param ->
                                ScopedDependencyContainer.provide(
                                    param.type.jvmErasure.java,
                                    context.activityId,
                                    context.viewModelId,
                                )
                            }
                        constructor.callBy(args)
                    }

                injectField(createdInstance, context)
                createdInstance
            }

        when (scope) {
            Scope.APPLICATION -> {
                ScopedDependencyContainer.registerToApplication(clazz, instance)
            }

            Scope.ACTIVITY -> {
                context.activityId?.let { activityId ->
                    ScopedDependencyContainer.registerToActivity(activityId, clazz, instance)
                } ?: run {
                    ScopedDependencyContainer.registerToApplication(clazz, instance)
                }
            }

            Scope.VIEWMODEL -> {
                context.viewModelId?.let { viewModelId ->
                    ScopedDependencyContainer.registerToViewModel(viewModelId, clazz, instance)
                } ?: run {
                    ScopedDependencyContainer.registerToApplication(clazz, instance)
                }
            }
        }

        return instance
    }

    inline fun <reified T : Any> injectField(
        target: T,
        context: InjectionContext = getContext(),
    ) {
        val fields =
            target::class
                .declaredMemberProperties
                .filter { it.hasAnnotation<InjectField>() }

        if (fields.isEmpty()) return

        fields.forEach { field ->
            val fieldClass = field.returnType.classifier as? KClass<*> ?: return@forEach

            val qualifierAnnotation =
                field.annotations.firstOrNull { annotation ->
                    annotation.annotationClass.hasAnnotation<Qualifier>()
                }

            val instance =
                if (qualifierAnnotation != null) {
                    ScopedDependencyContainer.provide(
                        fieldClass.java,
                        context.activityId,
                        context.viewModelId,
                        qualifier = qualifierAnnotation.annotationClass,
                    )
                } else {
                    if (fieldClass.isAbstract || fieldClass.java.isInterface) {
                        try {
                            ScopedDependencyContainer.provide(
                                fieldClass.java,
                                context.activityId,
                                context.viewModelId,
                            )
                        } catch (e: IllegalStateException) {
                            throw IllegalStateException(
                                "${fieldClass.simpleName} 인터페이스의 구현체가 등록되지 않음" +
                                    "구현체를 startMoong()에서 등록하거나, 필드에 구체 클래스를 사용",
                                e,
                            )
                        }
                    } else {
                        try {
                            ScopedDependencyContainer.provide(
                                fieldClass.java,
                                context.activityId,
                                context.viewModelId,
                            )
                        } catch (e: IllegalStateException) {
                            fieldClass.instantiate(context)
                        }
                    }
                }

            val javaField =
                field.javaField ?: field.javaGetter?.let { getter ->
                    target::class.java.getDeclaredField(field.name)
                } ?: return@forEach

            javaField.isAccessible = true
            javaField.set(target, instance)
        }
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
