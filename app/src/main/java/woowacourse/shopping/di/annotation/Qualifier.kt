package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.PROPERTY)
annotation class Qualifier(val implementation: String)
