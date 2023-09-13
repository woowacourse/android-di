package woowacourse.shopping.annotation

@Target(AnnotationTarget.CLASS)
annotation class KoalaViewModel

@Target(AnnotationTarget.PROPERTY)
annotation class KoalaRepository

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.PROPERTY)
annotation class KoalaQualifier
