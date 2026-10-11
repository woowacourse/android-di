package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import kotlin.collections.forEach
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    private val scopes = mutableMapOf<ScopeKey, Scope>()
    private val bindingScopes = mutableMapOf<DependencyKey<*>, ScopeType>()
    private val creating = mutableSetOf<DependencyKey<*>>()
    private val bindings = mutableMapOf<DependencyKey<*>, KClass<*>>()

    fun openScope(key: ScopeKey, parentKey: ScopeKey? = null) {
        check(key !in scopes)
        if (parentKey != null) check(parentKey in scopes)
        scopes[key] = Scope(key, parentKey)
    }

    fun closeScope(key: ScopeKey) {
        scopes.remove(key)?.clear()
    }

    fun <T : Any> bind(
        type: KClass<T>,
        implementation: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
        scopeType: ScopeType? = null,
    ) {
        val key = DependencyKey(type, qualifier)
        bindings[key] = implementation
        if (scopeType != null) {
            bindingScopes[key] = scopeType
            bindingScopes[DependencyKey(implementation, qualifier)] = scopeType
        }
    }

    fun <T : Any> bindInstance(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
        scopeKey: ScopeKey,
    ) {
        val key = DependencyKey(type, qualifier)
        val scope = scopes[scopeKey] ?: error("열리지 않은 스코프입니다: $scopeKey")
        scope.put(key, instance)
        bindingScopes[key] = scopeKey.type
    }

    fun <T : Any> resolve(
        key: DependencyKey<T>,
        scopeKey: ScopeKey,
    ): T {
        var ownerKey = scopeKey
        val requiredType = bindingScopes[key]

        if (requiredType != null) {
            while (ownerKey.type != requiredType) {
                ownerKey = scopes[ownerKey]?.parentKey ?: error("$key 의 스코프를 찾을 수 없습니다")
            }
        }

        val scope = scopes[ownerKey] ?: error("열리지 않은 스코프입니다: $scopeKey")

        scope.get(key)?.let { return it }

        val modelClass = key.type
        val qualifier = key.qualifier

        if (key in creating) {
            throw IllegalStateException("순환 의존성이 발생했어요: $modelClass")
        }

        creating += key

        try {
            val implementationClass =
                if (modelClass.java.isInterface) {
                    findImplementation(key)
                } else {
                    modelClass
                }
            val constructor = implementationClass.primaryConstructor ?: throw IllegalArgumentException("생성자를 찾을 수 없어요 : $modelClass")
            val dependencies = findDependencies(constructor, ownerKey)
            val instance = constructor.call(*dependencies.toTypedArray())

            injectFields(instance, ownerKey)

            scope.put(key, instance)
            if (modelClass != implementationClass) {
                val implementationKey = DependencyKey(implementationClass, qualifier) as DependencyKey<T>
                scope.put(implementationKey, instance)
            }
            return instance
        } finally {
            creating -= key
        }
    }

    private fun <T : Any> injectFields(
        instance: T,
        scopeKey: ScopeKey,
    ) {
        instance::class
            .memberProperties
            .filter { property ->
                property.annotations.any { it.annotationClass == Inject::class } &&
                        property is KMutableProperty1<*, *>
            }.forEach { property ->
                val mutableProperty = property as KMutableProperty1<*, *>
                val dependencyType = property.returnType.classifier as KClass<*>
                val qualifier =
                    property.annotations
                        .firstOrNull { annotation ->
                            annotation.annotationClass.annotations.any {
                                it.annotationClass == Qualifier::class
                            }
                        }?.annotationClass
                val dependencyKey = DependencyKey(dependencyType, qualifier)
                val dependency = resolve(dependencyKey, scopeKey)
                mutableProperty.setter.call(instance, dependency)
            }
    }

    private fun <T : Any> findImplementation(key: DependencyKey<T>): KClass<out T> {
        val qualifier = key.qualifier
        val modelClass = key.type

        if (qualifier == null) {
            val candidates =
                bindings
                    .filterKeys { key -> key.type == modelClass }
                    .values

            if (candidates.size > 1) {
                throw IllegalArgumentException("같은 타입의 구현체가 둘 이상이므로 Qualifier가 필요해요: $modelClass")
            }
        }

        return bindings[key] as? KClass<out T>
            ?: throw IllegalArgumentException("등록되지 않은 Qualifier예요: type = $modelClass, qualifier = $qualifier")
    }

    private fun <T : Any> findDependencies(
        constructor: KFunction<T>,
        scopeKey: ScopeKey,
    ): List<Any> =
        constructor.parameters.map { parameter ->
            val type = parameter.type.classifier as KClass<*>
            resolve(DependencyKey(type), scopeKey)
        }
}
