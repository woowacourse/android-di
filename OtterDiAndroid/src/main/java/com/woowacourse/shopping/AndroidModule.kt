package com.woowacourse.shopping

import android.content.Context
import woowacourse.shopping.otterdi.Module

interface AndroidModule : Module {
    var context: Context?
}
