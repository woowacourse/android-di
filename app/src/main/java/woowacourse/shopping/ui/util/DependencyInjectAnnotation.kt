package woowacourse.shopping.ui.util

@Target(AnnotationTarget.PROPERTY)
annotation class FieldInject

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ParamInject

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
)
annotation class Qualifier(val name: String = "")
