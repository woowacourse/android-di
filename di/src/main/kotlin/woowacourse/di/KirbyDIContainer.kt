package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class KirbyDIContainer {
    private val registry = DependencyRegistry()
    private val instanceStore = InstanceStore()

    fun createScope(): DependencyScope = DependencyScope(this)

    fun <T : Any> registerInstance(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) = registry.registerInstance(type, instance, qualifier)

    fun <T : Any> registerBinding(
        from: KClass<T>,
        to: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
        lifetime: DependencyLifetime = DependencyLifetime.EACH_SCOPE,
    ) = registry.registerBinding(from, to, qualifier, lifetime)

    /**
     * 최상위 객체를 매번 새로 생성하며, 주입하는 의존성은 수명 정책과 [scope]에 따라 재사용합니다.
     * [registerInstance]로 등록한 객체는 새로 생성할 수 없습니다.
     */
    fun <T : Any> createInstance(
        type: KClass<T>,
        scope: DependencyScope? = null,
    ): T {
        val store = storeFor(scope)
        val key = registry.selectKey(type, null)
        val registration = registry.registrationFor(key)
        require(registration !is Registration.Instance) { "등록된 인스턴스를 새로 생성할 수 없습니다: $key" }
        val target = (registration as? Registration.Binding)?.implementation ?: type
        val isContainerDependency = (registration as? Registration.Binding)?.lifetime == DependencyLifetime.CONTAINER

        @Suppress("UNCHECKED_CAST")
        return (instantiate(key, target, mutableSetOf(), store, isContainerDependency) as T).also { registry.markResolved(type) }
    }

    /**
     * 등록된 인스턴스 또는 수명 정책에 따라 보관된 객체를 반환하며, 없으면 생성해 저장합니다.
     * [scope]를 생략하면 컨테이너 저장소를 사용합니다.
     */
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scope: DependencyScope? = null,
    ): T {
        val store = storeFor(scope)
        val creatingKeys = mutableSetOf<DependencyKey>()
        val dependency = resolveDependency(type, qualifier, creatingKeys, store)

        @Suppress("UNCHECKED_CAST")
        return dependency as T
    }

    private fun storeFor(scope: DependencyScope?): InstanceStore {
        if (scope == null) {
            return instanceStore
        }

        require(scope.container === this) { "다른 컨테이너의 스코프를 사용할 수 없습니다" }
        require(!scope.isClosed) { "종료된 스코프를 사용할 수 없습니다" }
        return scope.instanceStore
    }

    private fun resolveDependency(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        creatingKeys: MutableSet<DependencyKey>,
        store: InstanceStore,
        isContainerDependency: Boolean = false,
    ): Any {
        val key = registry.selectKey(type, qualifier)
        registry.registeredInstance(key)?.let {
            registry.markResolved(type)
            return it
        }

        val binding = registry.registrationFor(key) as? Registration.Binding
        val lifetime = binding?.lifetime ?: DependencyLifetime.EACH_SCOPE
        require(!isContainerDependency || lifetime.sharesAcrossScopes) {
            "컨테이너 공유 의존성은 스코프 의존성을 참조할 수 없습니다: $key"
        }

        val selectedStore = if (lifetime.sharesAcrossScopes) instanceStore else store
        selectedStore.get(key)?.let {
            registry.markResolved(type)
            return it
        }

        val target = binding?.implementation ?: type
        return instantiate(key, target, creatingKeys, store, lifetime.sharesAcrossScopes).also {
            selectedStore.put(key, it)
            registry.markResolved(type)
        }
    }

    private fun instantiate(
        key: DependencyKey,
        target: KClass<*>,
        creatingKeys: MutableSet<DependencyKey>,
        store: InstanceStore,
        isContainerDependency: Boolean,
    ): Any {
        require(key !in creatingKeys) { "순환 의존성이 발견되었습니다: $key" }
        creatingKeys += key
        try {
            val constructor =
                target.primaryConstructor
                    ?: throw IllegalArgumentException("주 생성자가 없습니다: $target")
            val arguments =
                constructor.parameters.map { parameter ->
                    val parameterType =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $parameter")
                    resolveDependency(parameterType, qualifierOf(parameter.annotations), creatingKeys, store, isContainerDependency)
                }
            val instance = constructor.call(*arguments.toTypedArray())
            injectFields(instance, creatingKeys, store, isContainerDependency)
            return instance
        } finally {
            creatingKeys -= key
        }
    }

    private fun injectFields(
        target: Any,
        creatingKeys: MutableSet<DependencyKey>,
        store: InstanceStore,
        isContainerDependency: Boolean,
    ) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<*, *>>()
            .filter { it.findAnnotation<KirbyInject>() != null }
            .forEach { property ->
                property.isAccessible = true
                val dependencyType =
                    property.returnType.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $property")
                val dependency =
                    resolveDependency(
                        dependencyType,
                        qualifierOf(property.annotations),
                        creatingKeys,
                        store,
                        isContainerDependency,
                    )
                property.setter.call(target, dependency)
            }
    }

    private fun qualifierOf(annotations: List<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations.mapNotNull { annotation ->
                annotation.annotationClass.takeIf { it.findAnnotation<KirbyQualifier>() != null }
            }
        require(qualifiers.size <= 1) { "Qualifier를 둘 이상 지정할 수 없습니다: $qualifiers" }
        return qualifiers.singleOrNull()
    }
}
