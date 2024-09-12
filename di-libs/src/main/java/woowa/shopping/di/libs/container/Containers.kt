package woowa.shopping.di.libs.container

import org.jetbrains.annotations.VisibleForTesting
import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Container.Key
import woowa.shopping.di.libs.factory.InstanceFactory
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.qualify.Qualifier
import kotlin.reflect.KClass

object Containers {
    private val instanceRegistry = mutableMapOf<Key, InstanceFactory<*>>()
    private var isLocked: Boolean = false

    @OptIn(InternalApi::class)
    internal fun init(containers: List<Container>) {
        check(!isLocked) {
            "Containers 가 이미 초기화 되었습니다."
        }
        containers.flatMap { it.instanceRegistry.entries }
            .forEach { (key, factory) ->
                instanceRegistry[key] = factory
            }
        lockContainers()
    }

    @InternalApi
    fun <T : Any> resolve(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        lifecycle: Lifecycle = Lifecycle.SINGLETON,
    ): T {
        check(isLocked) {
            "Containers 가 초기화 되지 않았습니다. startDI()를 통해 초기화 해주세요."
        }
        val key = Key(clazz, qualifier, lifecycle)
        val factory = instanceRegistry[key]
        checkNotNull(factory) {
            "해당하는 인스턴스를 찾을 수 없습니다. $key"
        }
        return factory.instance() as T
    }

    private fun lockContainers() {
        isLocked = true
    }

    @VisibleForTesting
    fun clearContainersForTest() {
        instanceRegistry.clear()
        isLocked = false
    }
}
