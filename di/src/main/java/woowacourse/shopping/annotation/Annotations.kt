package woowacourse.shopping.annotation

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
annotation class Singleton

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR)
annotation class Inject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Target(AnnotationTarget.PROPERTY)
annotation class Binds

@Target(AnnotationTarget.FUNCTION)
annotation class Provides

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class ContextType

@ContextType
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class ApplicationContext

@ContextType
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class ActivityContext
