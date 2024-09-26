package com.zzang.di.testfixture

import com.zzang.di.annotation.lifecycle.ViewModelComponent

@ViewModelComponent
interface TestRepository {
    suspend fun test()
}
