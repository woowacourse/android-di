package com.kmlibs.supplin

object Injector {
    lateinit var instanceContainer: InstanceContainer
        private set

    fun init(block: InjectionBuilder.() -> Unit) {
        val injector = InjectionBuilder().apply(block).build()
        if (!::instanceContainer.isInitialized) {
            instanceContainer = InstanceContainer(injector.context, injector.modules)
        }
    }
}
