package woowacourse.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.di.annotation.InjectField
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

abstract class DIActivity(private val module: Module) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        module.setModuleContext(this)
        val container = Container(module)

        val properties =
            this::class.declaredMemberProperties.filter { it.hasAnnotation<InjectField>() }
        properties.forEach { property ->
            property.isAccessible = true
            val injectValue = container.getInstance(property.returnType.jvmErasure)
            (property as KMutableProperty<*>).setter.call(this, injectValue)
        }

//        val injector = Injector(container)
//        injector.injectField(this::class)
    }
}
