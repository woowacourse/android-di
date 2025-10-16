package woowacourse.shopping

import woowacourse.shopping.annotation.Inject

class CycleA
    @Inject
    constructor(
        val b: CycleB,
    )

class CycleB
    @Inject
    constructor(
        val a: CycleA,
    )
