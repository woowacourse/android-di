package com.bandal.halfmoon

import android.content.Context
import com.bandal.fullmoon.Module

interface AndroidDependencyModule : Module {
    var context: Context?

    fun getContext(): Context
}
