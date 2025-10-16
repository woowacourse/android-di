package woowacourse.shopping

import woowacourse.shopping.annotation.Inject

class TestService
    @Inject
    constructor(
        private val repository: TestRepository,
    ) {
        fun getServiceData() = repository.getData()
    }
