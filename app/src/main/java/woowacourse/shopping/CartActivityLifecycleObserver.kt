package woowacourse.shopping

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.anotations.ActivityScope
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class CartActivityLifecycleObserver(
    private val dateFormatter: DateFormatter,
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val parameters = this::class.primaryConstructor?.parameters ?: return

        parameters.forEach { kParameter ->
            AutoDIManager.registerDependency(kParameter.type.jvmErasure, dateFormatter)
        }
        injectDependencies(owner)
    }

    private fun injectDependencies(owner: LifecycleOwner) {
        val properties =
            owner::class.declaredMemberProperties
                .filter { it.hasAnnotation<ActivityScope>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(
                owner,
                AutoDIManager.dependencies[property.returnType.jvmErasure],
            )
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        deleteDependencies(owner)
    }

    private fun deleteDependencies(owner: LifecycleOwner) {
        val properties =
            owner::class.declaredMemberProperties
                .filter { it.hasAnnotation<ActivityScope>() }

        properties.forEach { property ->
            AutoDIManager.removeDependency(property.returnType.jvmErasure)
        }
    }
}
