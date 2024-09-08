package woowacourse.shopping.di.component

object DiSingletonComponent {
    private val binds: MutableMap<Class<*>, Any> = mutableMapOf()
    private const val ERROR_DI_MATCH = "No binding Singleton Component %s"

    fun <T : Any> bind(
        bindClassType: Class<T>,
        bindClass: T,
    ) {
        binds[bindClassType] = bindClass
    }

    fun <T : Any> match(bindClassType: Class<T>): T {
        return binds[bindClassType] as? T
            ?: throw IllegalArgumentException(ERROR_DI_MATCH.format(bindClassType))
    }
}
