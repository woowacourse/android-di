package com.bandal.halfmoon

import android.content.Context

class DefaultModule : AndroidDependencyModule {
    override var context: Context? = null

    override fun getContext(): Context =
        context ?: throw NullPointerException("context가 초기화 되지 않았습니다.")
}
