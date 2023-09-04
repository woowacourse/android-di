package woowacourse.shopping.util.autoDI

object AutoDI {
    operator fun invoke(init: AutoDI.() -> Unit) {
        this.init()
    }

    fun <T : Any> singleton(qualifier: String? = null, registerBlock: () -> T) {
        DependencyContainer.singletonRegister.register(qualifier, registerBlock)
    }

    fun <T : Any> disposable(qualifier: String? = null, registerBlock: () -> T){
        DependencyContainer.disposableRegister.register(qualifier,registerBlock)
    }
}
