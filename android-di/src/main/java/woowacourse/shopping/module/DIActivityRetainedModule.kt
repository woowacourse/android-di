package woowacourse.shopping.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import woowacourse.shopping.DIModule

@Parcelize
open class DIActivityRetainedModule(private val parentModule: DIApplicationModule) : DIModule(parentModule), Parcelable
