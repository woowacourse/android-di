package woowacourse.shopping.di.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
)
annotation class WooWaQualifier(val type: String) {
    companion object {
        const val DATABASE = "DATABASE"
        const val IN_MEMORY = "IN_MEMORY"
    }
}
