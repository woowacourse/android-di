package woowacourse.shopping.di

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance

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

                // ✅ Qualifier 확인
                val qualifier =
                    when {
                        javaField.isAnnotationPresent(RoomDatabase::class.java) -> RoomDatabase::class
                        javaField.isAnnotationPresent(InMemory::class.java) -> InMemory::class
                        else -> null
                    }

                val clazz = prop.returnType.classifier as? KClass<*>

                val dependency =
                    when (clazz) {
                        SavedStateHandle::class -> savedStateHandle
                        else -> clazz?.let { appContainer.resolve(it, qualifier) }
                    }

                if (dependency != null) {
                    prop.setter.call(vm, dependency)

                    Log.d(
                        "AppContainer",
                        "✅ ${kClass.simpleName}.${prop.name} <- ${dependency::class.simpleName}" +
                            (qualifier?.let { " (${it.simpleName})" } ?: ""),
                    )
                }
            }

        return vm
    }
}
