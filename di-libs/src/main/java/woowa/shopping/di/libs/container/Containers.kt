package woowa.shopping.di.libs.container

import org.jetbrains.annotations.VisibleForTesting
import kotlin.reflect.KClass

object Containers {
    private val containers: MutableList<Container> = mutableListOf()
    private var isLocked: Boolean = false

    fun <T : Any> get(clazz: KClass<T>): T {
        check(isLocked) {
            "Containers 가 초기화 되지 않았습니다. startDI()를 통해 초기화 해주세요."
        }
        return containers.first { it.cached.containsKey(clazz) }.get(clazz)
    }

    fun add(container: Container) {
        check(isLocked.not()) {
            "Containers 가 이미 초기화 되었습니다."
        }
        containers.add(container)
    }

    fun lockContainers() {
        isLocked = true
    }

    @VisibleForTesting
    fun clearContainersForTest() {
        containers.clear()
        isLocked = false
    }
}

inline fun startDI(block: Container.() -> Unit) {
    val container = Container()
    container.block()
    Containers.add(container)
    Containers.lockContainers()
}