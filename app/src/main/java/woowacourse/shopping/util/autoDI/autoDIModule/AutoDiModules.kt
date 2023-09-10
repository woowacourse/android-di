package woowacourse.shopping.util.autoDI.autoDIModule

fun autoDIModule(qualifier: String? = null, init: AutoDIModule.() -> Unit): AutoDIModule {
    val module = AutoDIModule(qualifier)
    module.init()
    return module
}
