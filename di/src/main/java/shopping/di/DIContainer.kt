package shopping.di

object DIContainer {
    private val appScopeInstances = mutableMapOf<Pair<Class<*>, QualifierType?>, Any>()
    private val activityScopes =
        mutableMapOf<ScopeOwner, MutableMap<Pair<Class<*>, QualifierType?>, Any>>()
    private val viewModelScopes =
        mutableMapOf<ScopeOwner, MutableMap<Pair<Class<*>, QualifierType?>, Any>>()

    fun <T : Any> register(
        clazz: Class<T>,
        instance: T,
        qualifier: QualifierType? = null,
        scope: Scope = Scope.APP,
        owner: ScopeOwner? = null
    ) {
        val key = clazz to qualifier
        when (scope) {
            Scope.APP -> appScopeInstances[key] = instance
            Scope.ACTIVITY -> {
                requireNotNull(owner) { "Activity scope requires a ScopeOwner" }
                val activityScope = activityScopes.getOrPut(owner) { mutableMapOf() }
                activityScope[key] = instance
            }

            Scope.VIEWMODEL -> {
                requireNotNull(owner) { "ViewModel scope requires a ScopeOwner" }
                val viewModelScope = viewModelScopes.getOrPut(owner) { mutableMapOf() }
                viewModelScope[key] = instance
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        clazz: Class<T>,
        qualifier: QualifierType? = null,
        scope: Scope = Scope.APP,
        owner: ScopeOwner? = null
    ): T {
        val key = clazz to qualifier
        val instance = when (scope) {
            Scope.APP -> appScopeInstances[key]
            Scope.ACTIVITY -> {
                requireNotNull(owner) { "Activity scope requires a ScopeOwner" }
                activityScopes[owner]?.get(key)
            }

            Scope.VIEWMODEL -> {
                requireNotNull(owner) { "ViewModel scope requires a ScopeOwner" }
                viewModelScopes[owner]?.get(key)
            }
        }

        return instance as? T
            ?: throw IllegalArgumentException("No instance found for ${clazz.name} in scope $scope")
    }

    fun <T : Any> createInstance(clazz: Class<T>, scope: Scope): T {
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


    fun injectFields(instance: Any) {
        val clazz = instance::class.java
        clazz.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true

                val qualifierAnnotation = field.getAnnotation(Qualifier::class.java)
                val qualifier = qualifierAnnotation?.value

                val scopeAnnotation = field.getAnnotation(ScopeAnnotation::class.java)
                val fieldScope = scopeAnnotation?.value ?: Scope.APP

                val owner = when (fieldScope) {
                    Scope.APP -> null
                    Scope.ACTIVITY, Scope.VIEWMODEL -> {
                        if (instance is ScopeOwner) instance else null
                    }
                }

                val fieldInstance = resolve(
                    field.type,
                    qualifier,
                    fieldScope,
                    owner
                )
                field.set(instance, fieldInstance)
            }
        }
    }

    fun createActivityScope(owner: ScopeOwner) {
        activityScopes[owner] = mutableMapOf()
    }

    fun createViewModelScope(owner: ScopeOwner) {
        viewModelScopes[owner] = mutableMapOf()
    }

    fun clearActivityScope(owner: ScopeOwner) {
        activityScopes.remove(owner)
    }

    fun clearViewModelScope(owner: ScopeOwner) {
        viewModelScopes.remove(owner)
    }
}
