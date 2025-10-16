package woowacourse.shopping

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Singleton

@Singleton
class AnnotatedSingletonService
    @Inject
    constructor()
