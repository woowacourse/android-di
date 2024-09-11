package woowacourse.shopping.di

/**
 * For supplying dependencies to classes.
 * This annotation can be used for field injection or constructor injection.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR)
annotation class Supply

/**
 * For providing application context as a parameter of module functions.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ApplicationContext

/**
 * For distinguishing modules that can provide instances needed for the program.
 */
@Target(AnnotationTarget.CLASS)
annotation class Module
