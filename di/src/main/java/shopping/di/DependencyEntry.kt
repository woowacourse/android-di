package shopping.di

data class DependencyEntry<T : Any>(
    val instance: T?,
    val clazz: Class<T>?,
    val qualifier: String?,
    val scope: Scope
)
