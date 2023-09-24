package woowacourse.di

import androidx.appcompat.app.AppCompatActivity
import woowacourse.di.annotation.InjectField
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

fun AppCompatActivity.injectProperties(container: Container) {
    val properties =
        this::class.declaredMemberProperties.filter { it.hasAnnotation<InjectField>() }
    properties.forEach { property ->
        property.isAccessible = true
        val injectValue = container.getInstance(property.returnType.jvmErasure)
        (property as KMutableProperty<*>).setter.call(this, injectValue)
    }
}
