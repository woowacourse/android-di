package woowacourse.shopping.di.module

import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class NormalModule : ActivityModule() {
    // 이 함수가 갖는 모든 종속항목을 메소드 매개변수로 나열해야 한다.
    fun getProductRepository(): ProductRepository {
        return getInstance { DefaultProductRepository() }
    }
}
