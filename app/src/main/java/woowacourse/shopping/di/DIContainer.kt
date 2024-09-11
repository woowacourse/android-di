package woowacourse.shopping.di

object DIContainer {
    private val instances = mutableMapOf<Pair<Class<*>, String?>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T, qualifier: String? = null) {
        instances[clazz to qualifier] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: Class<T>, qualifier: String? = null): T {
        return instances[clazz to qualifier] as? T
            ?: createInstance(clazz)
    }

    private fun <T : Any> createInstance(clazz: Class<T>): T {
        val constructor = clazz.constructors.firstOrNull()
            ?: throw IllegalArgumentException("No constructors found for class: ${clazz.name}")

        val params = constructor.parameterTypes.map { resolve(it) }.toTypedArray()
        val instance = constructor.newInstance(*params) as T
        injectFields(instance)
        register(clazz, instance)
        return instance
    }

    private fun <T : Any> injectFields(instance: T) {
        val clazz = instance::class.java
        clazz.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                val fieldInstance = resolve(field.type)
                field.set(instance, fieldInstance)
            }
        }
    }
}
