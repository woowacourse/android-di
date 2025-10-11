package woowacourse.shopping.di

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
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

class DependencyInjector(
    modules: List<DependencyModule>,
) {
    private val providers = ConcurrentHashMap<DependencyKey, LazyProvider>()
    private val instances = ConcurrentHashMap<DependencyKey, Instance>()

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

    fun get(type: KType): Instance = get(DependencyKey(type))

    fun get(
        type: KType,
        qualifier: String,
    ): Instance = get(DependencyKey(type, qualifier))

    private fun get(key: DependencyKey): Instance {
        return instances.computeIfAbsent(key) {
            val provider = providers[key]
            requireNotNull(provider?.invoke()) {
                val className = (key.type.classifier as? KClass<*>)?.simpleName ?: key.type
                val dependencyName =
                    key.qualifier?.let { "$className('$it')" } ?: className.toString()
                DEPENDENCY_NOT_FOUND.format(dependencyName)
            }
        }
    }

    fun inject(target: Instance) {
        target::class.declaredMemberProperties.forEach { property ->
            if (!hasInjectAnnotation(property)) return@forEach
            val qualifierName: String? = property.findAnnotation<Qualifier>()?.name

            injectDependency(target, property, qualifierName)
        }
    }

    private fun hasInjectAnnotation(property: KProperty1<out Any, *>): Boolean = property.findAnnotation<Inject>() != null

    private fun injectDependency(
        target: Instance,
        property: KProperty1<out Any, *>,
        qualifierName: String?,
    ) {
        property.javaField?.let { field ->
            val dependency =
                qualifierName?.let { get(property.returnType, it) }
                    ?: get(property.returnType)

            field.set(target, dependency)
        }
    }

    companion object {
        private const val DEPENDENCY_NOT_FOUND = "%s 타입의 의존성이 등록되어있지 않습니다."
    }
}
