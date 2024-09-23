package shopping.di

object DIContainer {
    private val appScopeInstances = mutableMapOf<Pair<Class<*>, QualifierType?>, Any>()
    private val activityScopeInstances = mutableMapOf<Pair<Class<*>, QualifierType?>, Any>()
    private val viewModelScopeInstances = mutableMapOf<Pair<Class<*>, QualifierType?>, Any>()

    fun <T : Any> register(
        clazz: Class<T>,
        instance: T,
        qualifier: QualifierType? = null,
        scope: Scope = Scope.APP
    ) {
        val key = clazz to qualifier
        when (scope) {
            Scope.APP -> appScopeInstances[key] = instance
            Scope.ACTIVITY -> activityScopeInstances[key] = instance
            Scope.VIEWMODEL -> viewModelScopeInstances[key] = instance
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        clazz: Class<T>,
        qualifier: QualifierType? = null,
        scope: Scope = Scope.APP
    ): T {
        val key = clazz to qualifier
        val instance = when (scope) {
            Scope.APP -> appScopeInstances[key]
            Scope.ACTIVITY -> activityScopeInstances[key]
            Scope.VIEWMODEL -> viewModelScopeInstances[key]
        }

        return instance as? T ?: createInstance(clazz, scope)
    }

    private fun <T : Any> createInstance(clazz: Class<T>, scope: Scope): T {
        val constructor = clazz.declaredConstructors.firstOrNull()
            ?: throw IllegalArgumentException("No constructors found for class: ${clazz.name}")

        val parameterTypes = constructor.parameterTypes
        val parameterAnnotations = constructor.parameterAnnotations

        val params = parameterTypes.mapIndexed { index, paramType ->
            val annotations = parameterAnnotations[index]
            var qualifier: QualifierType? = null
            var paramScope = scope

            for (annotation in annotations) {
                val annotationType = annotation.annotationClass.java
                when (annotation) {
                    is Qualifier -> {
                        qualifier = annotation.value
                    }

                    is ScopeAnnotation -> {
                        paramScope = annotation.value
                    }
                }
            }

            resolve(paramType, qualifier, paramScope)
        }.toTypedArray()

        val instance = constructor.newInstance(*params) as T
        injectFields(instance)
        register(clazz, instance, scope = scope)
        return instance
    }


    private fun injectFields(instance: Any) {
        val clazz = instance::class.java
        clazz.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true

                val qualifierAnnotation = field.getAnnotation(Qualifier::class.java)
                val qualifier = qualifierAnnotation?.value

                val scopeAnnotation = field.getAnnotation(ScopeAnnotation::class.java)
                val fieldScope = scopeAnnotation?.value ?: Scope.APP

                val fieldInstance = resolve(field.type, qualifier, fieldScope)
                field.set(instance, fieldInstance)
            }
        }
    }


    fun clearActivityScope() {
        activityScopeInstances.clear()
    }

    fun clearViewModelScope() {
        viewModelScopeInstances.clear()
    }

    fun clearAllScopes() {
        appScopeInstances.clear()
        activityScopeInstances.clear()
        viewModelScopeInstances.clear()
    }

}
