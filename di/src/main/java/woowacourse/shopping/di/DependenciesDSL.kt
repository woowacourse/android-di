package woowacourse.shopping.di

fun dependencies(
    init: Dependencies.() -> Unit
) {
    DependencyInjector.dependencies = Dependencies().apply {
        init()
    }
}
