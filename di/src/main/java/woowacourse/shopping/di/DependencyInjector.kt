package woowacourse.shopping.di

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

class DependencyInjector(
    modules: List<DependencyModule>,
) {
    private val providers = mutableMapOf<KType, () -> Any?>()
    private val instances = mutableMapOf<KType, Any>()

    init {
        modules.forEach {
            registerModule(it)
        }
    }

    private fun registerModule(module: DependencyModule) {
        module::class.memberProperties.forEach { property ->
            val kType = property.returnType
            providers[kType] = { property.getter.call(module) }
        }
    }

    fun get(type: KType): Any =
        instances[type] ?: run {
            val instance =
                requireNotNull(providers[type]?.invoke()) {
                    val className = (type.classifier as? KClass<*>)?.simpleName ?: type
                    DEPENDENCY_NOT_FOUND.format(className)
                }

            instances[type] = instance
            return instance
        }

    fun inject(target: Any) {
        target::class.declaredMemberProperties.forEach { property ->
            if (!hasInjectAnnotation(property)) return@forEach

            property.javaField?.let { field ->
                val dependency = get(property.returnType)
                field.set(target, dependency)
            }
        }
    }

    private fun hasInjectAnnotation(property: KProperty1<out Any, *>): Boolean {
        val javaField = property.javaField
        val hasInjectAnnotation =
            property.findAnnotation<Inject>() != null ||
                javaField?.getAnnotation(Inject::class.java) != null
        return hasInjectAnnotation
    }

    companion object {
        private const val DEPENDENCY_NOT_FOUND = "%s 타입의 의존성이 등록되어있지 않습니다."
    }
}
