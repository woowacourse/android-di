package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class CartActivityLifecycleObserver(
    private val context: Context,
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        injectDateFormatter(owner)
    }

    private fun injectDateFormatter(owner: LifecycleOwner) {
        val dateFormatter = DateFormatter(context)
        val property =
            owner::class.declaredMemberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .first { it.returnType.jvmErasure == DateFormatter::class }

        property.isAccessible = true
        property.setter.call(owner, dateFormatter)
    }
}
