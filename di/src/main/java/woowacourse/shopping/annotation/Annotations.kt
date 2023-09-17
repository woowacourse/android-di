package woowacourse.shopping.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Lifecycle

@Lifecycle
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class ApplicationLifecycle

@Lifecycle
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class ActivityLifecycle(val name: String)

@Lifecycle
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class ViewModelLifecycle(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Qualifier(val name: String)
