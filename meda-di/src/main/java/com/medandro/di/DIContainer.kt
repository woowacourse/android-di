package com.medandro.di

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.medandro.di.annotation.InjectField
import com.medandro.di.annotation.LifecycleScope
import com.medandro.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    private val applicationContext: Context,
    vararg registerClasses: KClass<*>,
) {
    private val applicationInstances = mutableMapOf<DependencyKey, Any>()
    private val activityScopedInstances =
        mutableMapOf<ComponentActivity, MutableMap<DependencyKey, Any>>()
    private val viewModelScopedInstances = mutableMapOf<ViewModel, MutableMap<DependencyKey, Any>>()
    private val interfaceMapping = mutableMapOf<DependencyKey, KClass<*>>()

    init {
        generateInterfaceMapping(registerClasses)
        globalContainers.add(this)
    }

    fun registerSingleton(
        instance: Any,
        qualifier: String? = null,
    ): DIContainer {
        // 구현체 자신의 타입으로 등록
        applicationInstances[DependencyKey(instance::class, qualifier)] = instance

        // 상위 타입들(인터페이스 포함)도 같이 등록
        instance::class.supertypes.forEach { superType ->
            val superClass = superType.classifier as? KClass<*>
            if (superClass != null && superClass != Any::class) {
                applicationInstances[DependencyKey(superClass, qualifier)] = instance
            }
        }
        return this
    }

    fun injectFields(target: Any) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { it.hasAnnotation<InjectField>() }
            .forEach { property ->
                val annotation = property.findAnnotation<InjectField>()
                val scope =
                    when (annotation?.scope) {
                        LifecycleScope.AUTO, null -> {
                            when (target) {
                                is ComponentActivity -> LifecycleScope.ACTIVITY
                                is ViewModel -> LifecycleScope.VIEWMODEL
                                else -> LifecycleScope.APPLICATION
                            }
                        }

                        LifecycleScope.APPLICATION -> LifecycleScope.APPLICATION
                        LifecycleScope.ACTIVITY -> LifecycleScope.ACTIVITY
                        LifecycleScope.VIEWMODEL -> LifecycleScope.VIEWMODEL
                    }

                injectFieldWithScope(target, property, scope)
            }
        // 액티비티 생명주기 관찰자 자동 등록
        if (target is ComponentActivity) {
            target.lifecycle.addObserver(
                ActivityScopeManager(
                    this,
                    target,
                ),
            )
        }
    }

    fun clearActivityScope(activity: ComponentActivity) {
        activityScopedInstances.remove(activity)?.clear()
    }

    fun getInstance(
        dependencyKey: DependencyKey,
        scope: LifecycleScope = LifecycleScope.APPLICATION,
        context: Any? = null,
    ): Any {
        // 인터페이스일 경우 매핑된 클래스로 반환
        interfaceMapping[dependencyKey]?.let { implClass ->
            val implKey = DependencyKey(implClass, dependencyKey.qualifier)
            return getInstance(implKey, scope, context)
        }

        // DIContainer 내부에서 생성&관리되는 인스턴스에서 반환
        val instances = getInstanceStorage(scope, context)
        instances[dependencyKey]?.let { return it }

        // 인스턴스가 존재하지 않을 경우 생성
        val createdInstance = createNewInstance(dependencyKey.type, scope, context)
        instances[dependencyKey] = createdInstance
        return createdInstance
    }

    private fun injectFieldWithScope(
        target: Any,
        property: KMutableProperty1<Any, Any?>,
        scope: LifecycleScope,
    ) {
        try {
            val fieldType = property.returnType.classifier as KClass<*>
            val qualifier = getFieldQualifier(property)
            val dependencyKey = DependencyKey(fieldType, qualifier)

            // 스코프에 따라 적절한 컨텍스트 전달
            val context =
                when (scope) {
                    LifecycleScope.ACTIVITY -> target as? ComponentActivity
                    LifecycleScope.VIEWMODEL -> target as? ViewModel
                    else -> null
                }

            val instance = getInstance(dependencyKey, scope, context)

            property.isAccessible = true
            property.set(target, instance)
        } catch (e: Exception) {
            throw IllegalStateException(
                "${target::class.simpleName}의 '${getFieldQualifier(property)} Qualifier의 ${property.name}, 필드 주입 실패 : ${e.message}",
                e,
            )
        }
    }

    private fun getInstanceStorage(
        scope: LifecycleScope,
        context: Any? = null,
    ): MutableMap<DependencyKey, Any> =
        when (scope) {
            LifecycleScope.APPLICATION -> applicationInstances
            LifecycleScope.ACTIVITY -> {
                val activity =
                    context as? ComponentActivity
                        ?: throw IllegalStateException(
                            "ACTIVITY 스코프는 Activity에서만 사용할 수 있습니다. " +
                                "현재 컨텍스트: ${context?.javaClass?.simpleName ?: "null"}",
                        )
                activityScopedInstances.getOrPut(activity) { mutableMapOf() }
            }

            LifecycleScope.VIEWMODEL -> {
                val viewModel =
                    context as? ViewModel
                        ?: throw IllegalStateException(
                            "VIEWMODEL 스코프는 ViewModel에서만 사용할 수 있습니다. " +
                                "현재 컨텍스트: ${context?.javaClass?.simpleName ?: "null"}",
                        )
                viewModelScopedInstances.getOrPut(viewModel) { mutableMapOf() }
            }

            LifecycleScope.AUTO -> throw IllegalStateException("AUTO 스코프는 실제 스코프로 변환된 후 사용되어야 합니다.")
        }

    private fun generateInterfaceMapping(registerClasses: Array<out KClass<*>>) {
        registerClasses.forEach { implClass ->
            val qualifier = getQualifier(implClass)
            val interfaces =
                implClass.supertypes
                    .mapNotNull { it.classifier as? KClass<*> }
                    .filter { it.java.isInterface }

            interfaces.forEach { interfaceClass ->
                val key = DependencyKey(interfaceClass, qualifier)
                interfaceMapping[key] = implClass
            }
        }
    }

    private fun createNewInstance(
        kClass: KClass<*>,
        scope: LifecycleScope,
        context: Any? = null,
    ): Any {
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalStateException(
                    "${kClass.simpleName} 는 주 생성자가 없어 자동으로 인스턴스를 생성할 수 없습니다." +
                        "DIContainer가 지원하는 타입: 주 생성자가 명시되어 있거나 생략된 클래스",
                )

        val parameterMap =
            constructor.parameters
                .filterNot { it.isOptional }
                .associateWith { param ->
                    val paramType = param.type.classifier as KClass<*>

                    when {
                        paramType == Context::class -> {
                            when (scope) {
                                LifecycleScope.ACTIVITY ->
                                    context as? ComponentActivity
                                        ?: throw IllegalStateException(
                                            "${kClass.simpleName} 생성 시 Activity Context가 필요하지만 Activity 컨텍스트를 찾을 수 없습니다." +
                                                "ACTIVITY 스코프에서만 Activity Context를 사용할 수 있습니다.",
                                        )

                                else -> applicationContext
                            }
                        }

                        else -> getInstance(DependencyKey(paramType), scope, context)
                    }
                }
        return constructor.callBy(parameterMap)
    }

    private fun getFieldQualifier(property: KMutableProperty1<Any, Any?>): String? = property.findAnnotation<Qualifier>()?.value

    private fun getQualifier(kClass: KClass<*>): String? = kClass.findAnnotation<Qualifier>()?.value

    private fun clearViewModelScope(viewModel: ViewModel) {
        viewModelScopedInstances.remove(viewModel)?.clear()
    }

    companion object {
        private val globalContainers = mutableSetOf<DIContainer>()

        fun clearViewModelScopeGlobally(viewModel: ViewModel) {
            globalContainers.forEach { it.clearViewModelScope(viewModel) }
        }
    }
}
