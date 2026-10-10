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

    private val applicationScope = DependencyScope()

    fun createScope(): DependencyScope = DependencyScope(applicationScope)

    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scope: DependencyScope = applicationScope,
    ): T = type.java.cast(resolve(type, qualifier, scope))

    private fun resolve(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
        scope: DependencyScope = applicationScope,
    ): Any {
        scope.find(DependencyKey(type, qualifier))?.let { return it }

        val registeredBindings =
            scope
                .keys()
                .filterIsInstance<DependencyKey>()
                .filter { it.type == type }

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

                resolve(dependencyType, scope = scope)
            }

        return constructor.call(*arguments.toTypedArray()).also { instance ->
            injectFields(instance, scope)
            scope.put(DependencyKey(type), instance)
        }
    }

    private fun injectFields(
        instance: Any,
        scope: DependencyScope = applicationScope,
    ) {
        instance::class.java.declaredFields
            .filter { field ->
                field.isAnnotationPresent(Inject::class.java)
            }.forEach { field ->
                val qualifier =
                    field.annotations
                        .firstOrNull {
                            it.annotationClass.java.isAnnotationPresent(Qualifier::class.java)
                        }?.annotationClass

                val dependency = resolve(field.type.kotlin, qualifier, scope)

                field.isAccessible = true
                field.set(instance, dependency)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scope = createScope()
        val viewModel = create(modelClass, scope)
        viewModel.addCloseable(scope)
        return viewModel
    }

    fun <T : ViewModel> create(
        modelClass: Class<T>,
        scope: DependencyScope,
    ): T {
        val viewModel =
            modelClass.kotlin
                .primaryConstructor
                ?.call()
                ?: throw IllegalArgumentException()

        injectFields(viewModel, scope)

        return modelClass.cast(viewModel)!!
    }

    fun register(
        type: KClass<*>,
        qualifier: KClass<out Annotation>,
        dependency: Any,
        scope: DependencyScope = applicationScope,
    ) {
        scope.put(DependencyKey(type, qualifier), dependency)
    }

    fun register(
        type: KClass<*>,
        dependency: Any,
        scope: DependencyScope = applicationScope,
    ) {
        scope.put(DependencyKey(type), dependency)
    }
}
