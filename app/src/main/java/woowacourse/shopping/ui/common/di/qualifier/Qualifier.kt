package woowacourse.shopping.ui.common.di.qualifier

import com.woowacourse.bunadi.annotation.Qualifier
import woowacourse.shopping.data.repository.InMemoryCartRepository

annotation class InMemoryCartRepositoryQualifier

annotation class DatabaseCartRepositoryQualifier

@Qualifier(InMemoryCartRepository::class)
annotation class InMemoryCartRepositoryQualifier2
