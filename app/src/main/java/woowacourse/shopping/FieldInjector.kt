package woowacourse.shopping

import woowacourse.shopping.ui.vmfactory.Inject

object FieldInjector {
    fun inject(target: Any, container: ShoppingContainer) {
        val clazz = target.javaClass
        for (field in clazz.declaredFields) {
            if (field.isAnnotationPresent(Inject::class.java)) {
                val dependency = container.get(field.type.kotlin)
                field.set(target, dependency)
            }
        }
    }
}