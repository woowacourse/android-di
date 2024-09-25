package woowacourse.shopping

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.alsonglibrary2.di.anotations.ActivityScope
import woowacourse.shopping.ShoppingApplication.Companion.dateFormatter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class CartActivityLifecycleObserver : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        injectDateFormatter(owner)
    }

    private fun injectDateFormatter(owner: LifecycleOwner) {
        val properties =
            owner::class.declaredMemberProperties
                .filter { it.hasAnnotation<ActivityScope>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(owner, dateFormatter)
        }
    }
}
