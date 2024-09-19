package shopping.di

object DIContainer {
    private val appScopeInstances = mutableMapOf<Pair<Class<*>, String?>, Any>()
    private val activityScopeInstances = mutableMapOf<Pair<Class<*>, String?>, Any>()
    private val viewModelScopeInstances = mutableMapOf<Pair<Class<*>, String?>, Any>()

    fun <T : Any> register(
        clazz: Class<T>,
        instance: T,
        qualifier: String? = null,
        scope: Scope = Scope.APP
    ) {
        when (scope) {
            Scope.APP -> appScopeInstances[clazz to qualifier] = instance
            Scope.ACTIVITY -> activityScopeInstances[clazz to qualifier] = instance
            Scope.VIEWMODEL -> viewModelScopeInstances[clazz to qualifier] = instance
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: Class<T>, qualifier: String? = null, scope: Scope = Scope.APP): T {
        val instance = when (scope) {
            Scope.APP -> appScopeInstances[clazz to qualifier]
            Scope.ACTIVITY -> activityScopeInstances[clazz to qualifier]
            Scope.VIEWMODEL -> viewModelScopeInstances[clazz to qualifier]
        }

        return instance as? T ?: createInstance(clazz, scope)
    }

    private fun <T : Any> createInstance(clazz: Class<T>, scope: Scope): T {
        val constructor = clazz.constructors.firstOrNull()
            ?: throw IllegalArgumentException("No constructors found for class: ${clazz.name}")

        val params = constructor.parameterTypes.map { resolve(it) }.toTypedArray()
        val instance = constructor.newInstance(*params) as T
        injectFields(instance)
        register(clazz, instance, scope = scope)
        return instance
    }

    private fun <T : Any> injectFields(instance: T) {
        val clazz = instance::class.java
        clazz.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true

                val qualifier = field.getAnnotation(Qualifier::class.java)?.value
                val scope = field.getAnnotation(ScopeAnnotation::class.java)?.value ?: Scope.APP

                val fieldInstance = resolve(field.type, qualifier, scope)
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
