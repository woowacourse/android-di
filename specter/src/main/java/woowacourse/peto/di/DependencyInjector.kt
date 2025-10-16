package woowacourse.peto.di

object DependencyInjector {
    lateinit var dependencyContainer: DependencyContainer
        private set

    fun init(dependencyContainer: DependencyContainer) {
        if (!::dependencyContainer.isInitialized) {
            this.dependencyContainer = dependencyContainer
        }
    }
}
