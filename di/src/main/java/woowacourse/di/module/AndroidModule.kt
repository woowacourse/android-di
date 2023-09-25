package woowacourse.di.module

import android.content.Context

interface AndroidModule : Module {
    var context: Context
    fun setModuleContext(context: Context)
}
