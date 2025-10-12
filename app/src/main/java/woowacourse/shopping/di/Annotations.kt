package woowacourse.shopping.di

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.PROPERTY)
annotation class InjectableViewModel

@Target(AnnotationTarget.PROPERTY)
annotation class Dependency

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier
