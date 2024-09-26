package shopping.di

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScopeAnnotation(
    val value: Scope,
)
