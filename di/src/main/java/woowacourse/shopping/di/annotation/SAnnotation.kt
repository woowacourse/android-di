package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class Inject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier
