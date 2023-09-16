package woowacourse.shopping.annotation

@Target(AnnotationTarget.CLASS)
annotation class SingleInstance

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Qualifier(val name: String)
