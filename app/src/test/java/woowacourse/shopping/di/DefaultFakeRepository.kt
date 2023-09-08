package woowacourse.shopping.di

class DefaultFakeRepository(
    val firstDataSource: FirstDataSource,
    val secondDataSource: SecondDataSource,
) : FakeRepository
