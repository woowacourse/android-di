package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.PROPERTY)
annotation class FieldInject

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ParamInject

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ApplicationContext
