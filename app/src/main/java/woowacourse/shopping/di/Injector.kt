package woowacourse.shopping.di

object Injector {
    lateinit var instanceContainer: InstanceContainer
        private set

    fun init(block: InjectionBuilder.() -> Unit) {
        val injector = InjectionBuilder().apply(block).build()
        instanceContainer = InstanceContainer(injector.context, injector.modules)
    }
}
