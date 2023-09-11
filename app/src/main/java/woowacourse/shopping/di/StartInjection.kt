package woowacourse.shopping.di

object StartInjection {
    operator fun invoke(block: StartInjection.() -> Unit) {
        this.block()
    }

    inline fun <reified T : Any> single(instance: Any) {
        DependencyContainer.add(T::class, instance)
    }

    // Disposable의 경우도 처리 필요
}
