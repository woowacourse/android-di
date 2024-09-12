package woowacourse.shopping.di

import com.example.di.Qualifier
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.local.LocalCartRepository

@Qualifier(LocalCartRepository::class)
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvideLocalCartRepository

@Qualifier(DefaultCartRepository::class)
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvideDefaultCartRepository
