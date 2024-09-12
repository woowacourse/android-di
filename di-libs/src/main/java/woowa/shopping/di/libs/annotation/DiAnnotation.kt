package woowa.shopping.di.libs.annotation

@RequiresOptIn(
    message = "Used to extend current API with Koin API. Shouldn't be used outside of DI API",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
)
annotation class InternalApi
