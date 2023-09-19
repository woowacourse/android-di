package woowacourse.shopping.fake.repository

const val FAKE_PRODUCT_REPOSITORY = "woowacourse.shopping.fake.repository.FakeProductRepository"
const val FAKE_CART_REPOSITORY = "woowacourse.shopping.fake.repository.FakeCartRepository"

interface FakeRepository

class FakeProductRepository : FakeRepository

class FakeCartRepository(private val dao: FakeCartProductDao) : FakeRepository
