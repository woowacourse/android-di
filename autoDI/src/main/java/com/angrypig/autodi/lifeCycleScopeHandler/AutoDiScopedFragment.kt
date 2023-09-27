package com.angrypig.autodi.lifeCycleScopeHandler

import androidx.fragment.app.Fragment

abstract class AutoDiScopedFragment<T : Fragment> : Fragment() {
    // todo 예외사항이 너무 많을것 같아서 일단 생략(Fragment는 정말 잘못만듬)
    abstract val registerScope: T

    override fun onDestroy() {
        super.onDestroy()
    }
}
