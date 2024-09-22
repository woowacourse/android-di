package woowacourse.shopping.di

@Target(AnnotationTarget.PROPERTY)
annotation class FieldInject

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ParamInject
