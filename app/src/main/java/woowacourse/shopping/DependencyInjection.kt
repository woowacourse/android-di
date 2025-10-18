package woowacourse.shopping

object DependencyInjection {
    fun <T : Any> inject(instance: T) {
        val fields = instance::class.java.declaredFields

        for (field in fields) {
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true

                val dependency = DIContainer.get(
                    field.type.kotlin,
                    Remote::class,
                )
                field.set(instance, dependency)
            }
        }
    }
}
