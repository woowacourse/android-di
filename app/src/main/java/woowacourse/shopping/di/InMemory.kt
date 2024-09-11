package woowacourse.shopping.di

@Qualifier
@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class InMemory
