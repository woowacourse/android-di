package woowacourse.peto.di

import woowacourse.peto.di.annotation.ActivityScope
import woowacourse.peto.di.annotation.Inject
import woowacourse.peto.di.annotation.Qualifier
import woowacourse.peto.di.annotation.Scope
import woowacourse.peto.di.annotation.ScopedInstanceProvider
import woowacourse.peto.di.annotation.SingletonScope
import woowacourse.peto.di.annotation.ViewModelScope
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

private typealias LazyProvider = () -> Any?
private typealias Instance = Any

class DependencyContainer(
    modules: List<DependencyModule>,
) {
    private val providers = ConcurrentHashMap<DependencyKey, LazyProvider>()
    private val instances = ScopedInstanceProvider()

    init {
        modules.forEach(::registerModule)
    }

    private fun registerModule(module: DependencyModule) {
        module::class.memberProperties.forEach { property ->
            val kType = property.returnType
            val qualifier = property.findAnnotation<Qualifier>()?.name
            val provider = { property.getter.call(module) }
            providers[DependencyKey(kType, qualifier)] = provider
        }
    }

    fun get(
        key: DependencyKey,
        scope: Scope,
    ): Instance {
        val provider =
            requireNotNull(providers[key]) {
                val className = (key.type.classifier as? KClass<*>)?.simpleName ?: key.type
                DEPENDENCY_NOT_FOUND.format(className)
            }
        val clazz =
            requireNotNull(key.type.classifier as? KClass<*>) {
                val className = (key.type.classifier as? KClass<*>)?.simpleName ?: key.type
                DEPENDENCY_NOT_VALID.format(className)
            }

        return when {
            clazz.findAnnotation<SingletonScope>() != null ->
                getOrCreateScopedInstance(scope, key, provider)

            clazz.findAnnotation<ActivityScope>() != null ->
                getOrCreateScopedInstance(scope, key, provider)

            clazz.findAnnotation<ViewModelScope>() != null ->
                getOrCreateScopedInstance(scope, key, provider)

            else -> provider() as Instance
        }
    }

    private fun getOrCreateScopedInstance(
        scope: Scope,
        key: DependencyKey,
        provider: LazyProvider,
    ): Any = instances.computeIfAbsent(scope, key, provider() as Instance)

    fun clearScope(scope: Scope) = instances.remove(scope)

    fun get(
        type: KType,
        scope: Scope,
    ): Instance = get(DependencyKey(type), scope)

    fun get(
        type: KType,
        qualifier: String,
        scope: Scope,
    ): Instance = get(DependencyKey(type, qualifier), scope)

    fun inject(
        target: Instance,
        scope: Scope,
    ) {
        target::class.declaredMemberProperties.forEach { property ->
            if (!hasInjectAnnotation(property)) return@forEach
            val qualifierName: String? = property.findAnnotation<Qualifier>()?.name
            injectDependency(target, property, qualifierName, scope)
        }
    }

    private fun hasInjectAnnotation(property: KProperty1<out Any, *>): Boolean = property.findAnnotation<Inject>() != null

    private fun injectDependency(
        target: Instance,
        property: KProperty1<out Any, *>,
        qualifierName: String?,
        scope: Scope,
    ) {
        property.javaField?.let { field ->
            field.isAccessible = true
            val dependency =
                qualifierName?.let { get(property.returnType, it, scope) }
                    ?: get(property.returnType, scope)

            field.set(target, dependency)
        }
    }

    companion object {
        private const val DEPENDENCY_NOT_FOUND = "%s 타입의 의존성이 등록 되어있지 않습니다."
        private const val DEPENDENCY_NOT_VALID = "%s 타입의 의존성이 유효하지 않습니다."
    }
}
