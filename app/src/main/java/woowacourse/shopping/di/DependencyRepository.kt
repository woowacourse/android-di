package woowacourse.shopping.di

interface DependencyRepository {
    fun get(key: DependencyKey): Any?

    fun save(
        key: DependencyKey,
        instance: Any,
    )
}
