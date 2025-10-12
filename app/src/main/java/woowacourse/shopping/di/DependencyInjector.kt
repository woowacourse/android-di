package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.QualifierTag
import woowacourse.shopping.di.definition.Qualifier

object DependencyInjector {
    fun <T : Any> injectFields(instance: T): T {
        val instanceClass: KClass<out T> = instance::class

        instanceClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .filter { it.isInjectableProperty() }
            .forEach { field ->
                val fieldClass: KClass<*> =
                    field.returnType.classifier as? KClass<*> ?: return@forEach
                val qualifier: Qualifier? = field.getQualifierOrNull()

                if (AppInjector.hasDefinition(fieldClass, qualifier)) {
                    field.apply {
                        isAccessible = true
                        setter.call(instance, AppInjector.get(fieldClass, qualifier))
                    }
                }
            }

        return instance
    }

    private fun <T : Any> KMutableProperty1<T, Any?>.isInjectableProperty() =
        this.isLateinit && this.javaField?.getAnnotation(Inject::class.java) != null

    private fun <T : Any> KMutableProperty1<T, Any?>.getQualifierOrNull(): Qualifier? {
        val qualifierName: String? = this.javaField?.getAnnotation(QualifierTag::class.java)?.value
        return qualifierName?.let { Qualifier.Named(it) }
    }
}
