package woowacourse.shopping.di

import com.example.di.Qualifier
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.local.LocalCartRepository

@Qualifier(LocalCartRepository::class)
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvideLocalCartRepository

@Qualifier(InMemoryCartRepository::class)
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvideInMemoryCartRepository
