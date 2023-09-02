package woowacourse.shopping.di

open class DiContainer {
    private val fields get() = this.javaClass.declaredFields

    private fun <T> getFromField(clazz: Class<T>): T? {
        return fields.firstOrNull { field ->
            field.isAccessible = true
            field.type.simpleName == clazz.simpleName
        }?.get(this) as T
    }

    fun <T> get(clazz: Class<T>): T {
        return getFromField(clazz)
            ?: throw IllegalArgumentException()
    }

    fun <T> inject(clazz: Class<T>): T {
        val constructor = clazz.declaredConstructors.first()

        return constructor.newInstance(
            *constructor.parameterTypes.map { get(it) }.toTypedArray()
        ) as T
    }
}
