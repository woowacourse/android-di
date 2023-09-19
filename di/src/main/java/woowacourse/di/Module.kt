package woowacourse.di

import android.content.Context

interface Module {
    var context: Context
    fun setModuleContext(context: Context)
}
