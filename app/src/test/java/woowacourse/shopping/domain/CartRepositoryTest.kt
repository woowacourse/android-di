package woowacourse.shopping.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.`when`
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.PRODUCT_A_1000

class CartRepositoryTest :
    BehaviorSpec({
        given("카트 저장소에") {
            lateinit var repository: CartRepository
            beforeContainer {
                repository = FakeCartRepository()
            }

            `when`("상품 하나를 추가하면") {
                repository.addCartProduct(PRODUCT_A_1000)
                val expectedName = "A"
                val expectedPrice = 1_000
                then("저장된 상품을 확인할 수 있다") {
                    val actual = repository.getAllCartProducts().first()
                    actual.name shouldBe expectedName
                    actual.price shouldBe expectedPrice
                }
                then("상품 1개가 있는 것을 확인할 수 있다") {
                    val actual = repository.getAllCartProducts()
                    actual shouldHaveSize 1
                }
            }

            `when`("상품을 제거하면") {
                repository.addCartProduct(PRODUCT_A_1000)
                val id = repository.getAllCartProducts().indexOfFirst { it == PRODUCT_A_1000 }
                repository.deleteCartProduct(id)
                then("정상적으로 삭제되었음을 확인할 수 있다") {
                    repository.getAllCartProducts().shouldBeEmpty()
                }
            }
        }
    })
