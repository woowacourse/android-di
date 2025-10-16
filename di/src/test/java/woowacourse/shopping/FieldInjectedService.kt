package woowacourse.shopping

import woowacourse.shopping.annotation.Inject

class FieldInjectedService {
    @Inject
    lateinit var repository: TestRepository

    fun getInjectedData() = repository.getData()
}
