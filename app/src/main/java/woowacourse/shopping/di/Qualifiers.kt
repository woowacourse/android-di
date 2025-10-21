package woowacourse.shopping.di

import com.example.di.Qualifier

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Qualifier
annotation class DatabaseRepository

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Qualifier
annotation class InMemoryRepository
