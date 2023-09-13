package woowacourse.shopping.ui.common.di.qualifier

import com.woowacourse.bunadi.annotation.Qualifier
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository

@Qualifier(DefaultProductRepository::class)
annotation class DefaultProductRepositoryQualifier

@Qualifier(InMemoryCartRepository::class)
annotation class InMemoryCartRepositoryQualifier

@Qualifier(DatabaseCartRepository::class)
annotation class DatabaseCartRepositoryQualifier
