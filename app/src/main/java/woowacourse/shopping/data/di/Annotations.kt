package woowacourse.shopping.data.di

import com.hyegyeong.di.annotations.Qualifier


@Qualifier
annotation class DatabaseCartRepository

@Qualifier
annotation class InMemoryCartRepository

@Qualifier
annotation class InMemoryProductRepository