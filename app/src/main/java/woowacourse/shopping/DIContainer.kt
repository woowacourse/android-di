package woowacourse.shopping

object DIContainer {
    private val instances = mutableMapOf<Class<*>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T) {
        instances[clazz] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: Class<T>): T {
        return instances[clazz] as? T ?: throw IllegalArgumentException("No instance found for ${clazz.simpleName}")
    }
}
