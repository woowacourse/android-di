package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.woowa.di.findQualifierClassOrNull
import com.woowa.di.injectFieldFromComponent
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

inline fun <reified T : ViewModel> getDIViewModelFactory(): ViewModelProvider.Factory {
    ViewModelComponentManager.createComponent(T::class)
    val instance = createInstance(T::class)
    injectFieldFromComponent<ViewModelComponentManager>(instance)
    removeInstancesOnCleared<T>(instance)
    return viewModelFactory {
        addInitializer(T::class) {
            instance
        }
    }
}

inline fun <reified T : ViewModel> createInstance(clazz: KClass<T>): T {
    val constructor = clazz.primaryConstructor ?: error("주 생성자가 없는 객체는 DI 주입을 할 수 없습니다.")
    val args =
        constructor.parameters.associateWith {
            ViewModelComponentManager.getDIInstance(
                it.type.jvmErasure,
                it.findQualifierClassOrNull(),
            )
        }
    return constructor.callBy(args)
}
