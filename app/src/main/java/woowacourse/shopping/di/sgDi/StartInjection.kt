package woowacourse.shopping.di.sgDi

object StartInjection {
    operator fun invoke(block: StartInjection.() -> Unit) {
        this.block()
    }

    inline fun <reified T : Any> single(qualifier: String, instance: Any) {
        DependencyContainer.add(qualifier, instance)
    }

    inline fun <reified T : Any> single(instance: Any) {
        DependencyContainer.add(T::class, instance)
    }

    // Disposable의 경우도 처리 필요
}
