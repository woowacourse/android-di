package woowacourse.di

class DependencyScope internal constructor(
    internal val container: KirbyDIContainer,
) : AutoCloseable {
    internal val instanceStore = InstanceStore()

    internal var isClosed = false
        private set

    override fun close() {
        if (isClosed) {
            return
        }

        instanceStore.clear()
        isClosed = true
    }
}
