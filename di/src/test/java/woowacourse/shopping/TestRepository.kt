package woowacourse.shopping

import woowacourse.shopping.annotation.Inject

interface TestRepository {
    fun getData(): String
}

class InMemoryTestRepository
    @Inject
    constructor() : TestRepository {
        override fun getData(): String = "InMemory"
    }

class RoomTestRepository
    @Inject
    constructor() : TestRepository {
        override fun getData(): String = "Room"
    }
