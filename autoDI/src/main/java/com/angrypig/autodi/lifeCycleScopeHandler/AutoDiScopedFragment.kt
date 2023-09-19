package com.angrypig.autodi.lifeCycleScopeHandler

import androidx.fragment.app.Fragment

abstract class AutoDiScopedFragment() : Fragment() {

    abstract val registerScope: Fragment

    override fun onDestroy() {
        super.onDestroy()
    }
}
