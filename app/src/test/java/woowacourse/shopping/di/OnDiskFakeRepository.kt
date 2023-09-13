package woowacourse.shopping.di

class OnDiskFakeRepository(
    val firstDataSource: FirstDataSource,
    val secondDataSource: SecondDataSource,
) : FakeRepository
