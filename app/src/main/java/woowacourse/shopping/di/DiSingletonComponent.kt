package woowacourse.shopping.di

object DiSingletonComponent {
    private val binds: MutableMap<Class<*>, Any> = mutableMapOf()

    fun <T> bind(
        bindClassType: Class<T>,
        bindClass: T,
    ) {
        binds[bindClassType] = bindClass as Any
    }

    private inline fun <reified T : Any> bind(instance: T) {
        binds[T::class.java] = instance
    }

    fun hasBinding(clazz: Class<*>) = binds.containsKey(clazz)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> match(bindClassType: Class<T>): T =
        binds[bindClassType] as? T
            ?: throw IllegalArgumentException("No binding for ${bindClassType.name}")

    fun matchRaw(bindClassType: Class<*>): Any =
        binds[bindClassType]
            ?: throw IllegalArgumentException("No binding for ${bindClassType.name}")
}
