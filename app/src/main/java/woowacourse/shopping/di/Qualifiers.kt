package woowacourse.shopping.di

import com.example.di.Qualifier

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class DatabaseRepository

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class InMemoryRepository
