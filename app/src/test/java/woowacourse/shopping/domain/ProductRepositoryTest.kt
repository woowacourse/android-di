package woowacourse.shopping.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import org.mockito.Mockito.`when`
import woowacourse.shopping.fixture.FakeProductRepository

class ProductRepositoryTest :
    BehaviorSpec({
        val repository: ProductRepository = FakeProductRepository()
        given("상품 저장소에서") {
            val expectedProductsSize = 2
            `when`("조회해서") {
                val products = repository.getAllProducts()
                then("데이터를 가져올 수 있다") {
                    products shouldHaveSize expectedProductsSize
                }
            }
        }
    })
