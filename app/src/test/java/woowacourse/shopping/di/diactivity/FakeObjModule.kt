package woowacourse.shopping.di.diactivity

import com.di.berdi.Module
import woowacourse.shopping.di.FakeObj

object FakeObjModule : Module {
    fun provideFakeObj(): FakeObj = FakeObj
}
