package woowacourse.shopping.di

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class Inject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier
