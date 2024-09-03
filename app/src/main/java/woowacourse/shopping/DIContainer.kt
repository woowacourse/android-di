package woowacourse.shopping

object DIContainer {
    private val instances = mutableMapOf<Class<*>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T) {
        instances[clazz] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: Class<T>): T {
        return instances[clazz] as? T ?: createInstance(clazz)
    }

    private fun <T : Any> createInstance(clazz: Class<T>): T {
        val constructor = clazz.constructors.first()
        val params = constructor.parameterTypes.map { resolve(it) }.toTypedArray()
        val instance = constructor.newInstance(*params) as T
        register(clazz, instance)
        return instance
    }
}
