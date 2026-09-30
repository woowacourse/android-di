package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyContainer : ViewModelProvider.Factory {
    private data class DependencyKey(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>? = null,
    )

    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Inject

    @Target(AnnotationTarget.ANNOTATION_CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Qualifier

    private val dependencies: MutableMap<DependencyKey, Any> =
        mutableMapOf()

    private fun resolve(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any {
        dependencies[DependencyKey(type, qualifier)]?.let { return it }

        val registeredBindings = dependencies.keys.filter { it.type == type }
        if (registeredBindings.isNotEmpty()) {
            val availableQualifiers =
                registeredBindings
                    .mapNotNull { it.qualifier?.simpleName }
                    .distinct()
                    .sorted()

            val message =
                if (qualifier == null && availableQualifiers.isNotEmpty()) {
                    "Qualifier required for ${type.qualifiedName}. Available qualifiers: ${availableQualifiers.joinToString()}"
                } else {
                    "No dependency registered for ${type.qualifiedName} with qualifier ${qualifier?.simpleName ?: "none"}"
                }

            throw IllegalArgumentException(message)
        }

        val constructor =
            type.primaryConstructor
                ?: throw IllegalArgumentException(
                    "No primary constructor for ${type.qualifiedName}",
                )

        val arguments =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException(
                            "Unsupported parameter: ${parameter.name}",
                        )

                resolve(dependencyType)
            }

        return constructor.call(*arguments.toTypedArray()).also { instance ->
            injectFields(instance)
            dependencies[DependencyKey(type)] = instance
        }
    }

    private fun injectFields(instance: Any) {
        instance::class.java.declaredFields
            .filter { field ->
                field.isAnnotationPresent(Inject::class.java)
            }.forEach { field ->
                val qualifier =
                    field.annotations
                        .firstOrNull {
                            it.annotationClass.java.isAnnotationPresent(Qualifier::class.java)
                        }?.annotationClass

                val dependency = resolve(field.type.kotlin, qualifier)

                field.isAccessible = true
                field.set(instance, dependency)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel =
            modelClass.kotlin
                .primaryConstructor
                ?.call()
                ?: throw IllegalArgumentException()

        injectFields(viewModel)

        return modelClass.cast(viewModel)!!
    }

    fun register(
        type: KClass<*>,
        qualifier: KClass<out Annotation>,
        dependency: Any,
    ) {
        dependencies[DependencyKey(type, qualifier)] = dependency
    }

    fun register(
        type: KClass<*>,
        dependency: Any,
    ) {
        dependencies[DependencyKey(type)] = dependency
    }
}
