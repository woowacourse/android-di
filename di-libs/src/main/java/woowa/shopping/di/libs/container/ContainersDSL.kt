package woowa.shopping.di.libs.container

import woowa.shopping.di.libs.annotation.InternalApi

class ContainersDSL {
    private val containers = mutableListOf<Container>()

    fun container(block: Container.() -> Unit) {
        val container = Container()
        container.block()
        containers.add(container)
    }

    fun containers(containers: List<Container>) {
        this.containers.addAll(containers)
    }

    internal fun init() {
        checkDuplicatedDependencies(containers)
        Containers.init(containers)
    }

    @OptIn(InternalApi::class)
    private fun checkDuplicatedDependencies(newContainers: List<Container>) {
        val duplicatedDependencies =
            newContainers
                .flatMap { it.instanceRegistry.keys }
                .groupBy { it }
                .filter { it.value.size > 1 }
                .keys

        check(duplicatedDependencies.isEmpty()) {
            "의존성이 중복되었습니다. $duplicatedDependencies"
        }
    }
}

fun startDI(block: ContainersDSL.() -> Unit) {
    ContainersDSL().apply(block).init()
}
