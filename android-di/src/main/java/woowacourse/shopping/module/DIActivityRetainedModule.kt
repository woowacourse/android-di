package woowacourse.shopping.module

import woowacourse.shopping.DIModule
import java.io.Serializable

open class DIActivityRetainedModule(private val parentModule: DIApplicationModule) : DIModule(parentModule), Serializable
