package woowacourse.shopping.core.di

@Target(AnnotationTarget.PROPERTY)
annotation class InjectPersistentCartRepository

@Target(AnnotationTarget.PROPERTY)
annotation class InjectInMemoryCartRepository
