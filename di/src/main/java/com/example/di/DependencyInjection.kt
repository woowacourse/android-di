package com.example.di

object DependencyInjection {
    fun <T : Any> inject(instance: T) {
        val fields = instance::class.java.declaredFields

        for (field in fields) {
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true

                val qualifier = field.annotations
                    .firstOrNull { it.annotationClass.annotations.any { meta -> meta.annotationClass.simpleName == "Qualifier" } }
                    ?.annotationClass ?: Remote::class

                val dependency = DIContainer.get(field.type.kotlin, qualifier)
                field.set(instance, dependency)
            }
        }
    }
}
