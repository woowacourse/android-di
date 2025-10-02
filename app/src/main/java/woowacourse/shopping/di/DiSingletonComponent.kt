package woowacourse.shopping.di

object DiSingletonComponent {
    private val binds: MutableMap<Class<*>, Any> = mutableMapOf()

    fun <T> bind(
        bindClassType: Class<T>,
        bindClass: Any,
    ) {
        binds[bindClassType] = bindClass
    }

    fun<T: Any> match(
        bindClassType: Class<T>,
    ): T {
        return binds[bindClassType] as? T
            ?: throw IllegalArgumentException("bindClassType: $bindClassType")
    }
}
