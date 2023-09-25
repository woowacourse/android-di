package woowacourse.shopping.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Lifecycle

@Lifecycle
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class ApplicationLifecycle

@Lifecycle
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class RetainedActivityLifecycle(val activityClassName: String)

@Lifecycle
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class ReleasedActivityLifecycle(val activityClassName: String)

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Qualifier
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class InMemory

@Qualifier
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Database

@Qualifier
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class ApplicationContext

@Qualifier
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class ActivityContext

annotation class Singleton
