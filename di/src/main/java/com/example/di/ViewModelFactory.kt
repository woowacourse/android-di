package com.example.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.isAccessible

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val kClass = modelClass.kotlin
        val vm = kClass.createInstance()
        val savedStateHandle = extras.createSavedStateHandle()

        kClass.members
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .forEach { prop ->
                val javaField = kClass.java.getDeclaredField(prop.name)
                if (!javaField.isAnnotationPresent(Inject::class.java)) return@forEach

                val qualifier =
                    when {
                        javaField.isAnnotationPresent(RoomDatabase::class.java) -> RoomDatabase::class
                        javaField.isAnnotationPresent(InMemory::class.java) -> InMemory::class
                        else -> null
                    }

                val clazz =
                    prop.returnType.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${prop.name} 타입 정보를 가져올 수 없습니다")

                val dependency =
                    when (clazz) {
                        SavedStateHandle::class -> savedStateHandle
                        else -> clazz.let { appContainer.resolve(it, qualifier) }
                    }

                prop.isAccessible = true
                prop.setter.call(vm, dependency)
            }

        return vm
    }
}
