package com.example.di.container

import android.content.Context
import com.example.di.module.ActivityModule
import com.example.di.module.ApplicationModule
import kotlin.reflect.full.primaryConstructor

class DiActivityModuleContainer(private val applicationModule: ApplicationModule) {
    private val moduleMap: MutableMap<Int, ActivityModule> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityModule> provideActivityModule(
        newOwner: Context,
        oldOwnerHashCode: Int?,
        clazz: Class<T>,
    ): T {
        var module = moduleMap.remove(oldOwnerHashCode)
        if (module == null) {
            module = createFromOwnerHashCode(newOwner, clazz)
        }
        module.context = newOwner
        moduleMap[newOwner.hashCode()] = module
        return module as T
    }

    private fun <T : ActivityModule> createFromOwnerHashCode(
        owner: Context,
        clazz: Class<T>,
    ): T {
        val primaryConstructor = clazz.kotlin.primaryConstructor
            ?: throw NullPointerException("액티비티 모듈의 주 생성자는 애플리케이션 모듈만 매개변수로 선언되어있어야 합니다.")
        val module = primaryConstructor.call(owner, applicationModule)
        moduleMap[owner.hashCode()] = module
        return module
    }

    fun removeModule(ownerHashCode: Int) {
        val module = moduleMap.remove(ownerHashCode)
        module?.context = null
    }
}
