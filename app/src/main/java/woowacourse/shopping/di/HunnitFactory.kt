package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object HunnitFactory : ViewModelProvider.Factory {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val resolvingPath = mutableListOf<KClass<*>>()
    private val implementations: Map<KClass<*>, KClass<*>> =
        mapOf(CartRepository::class to DefaultCartRepository::class)

    fun <T : Any> register(
        targetClass: KClass<T>,
        instance: T,
    ) {
        instances[targetClass] = instance
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = createInstance(modelClass.kotlin)

    fun <T : Any> createInstance(targetClass: KClass<T>): T {
        check(targetClass !in resolvingPath) {
            "의존성: ${(resolvingPath + targetClass).joinToString(" - ") { it.simpleName ?: it.toString() }}"
        }
        resolvingPath.add(targetClass)
        try {
            val constructor =
                targetClass.primaryConstructor
                    ?: throw IllegalArgumentException("$targetClass : 주 생성자를 찾을 수 없습니다.")

            val parameterTypes = constructor.parameters.map { it.type.classifier as KClass<*> }

            val instance = constructor.call(*parameterTypes.map { getInstance(it) }.toTypedArray())

            targetClass.java.declaredFields
                .filter { it.isAnnotationPresent(Inject::class.java) }
                .forEach { field ->
                    field.isAccessible = true
                    field.set(instance, getInstance(field.type.kotlin))
                }

            return instance
        } finally {
            resolvingPath.removeAt(resolvingPath.lastIndex)
        }
    }

    fun getInstance(kClass: KClass<*>): Any =
        instances.getOrPut(kClass) {
            createInstance(implementations[kClass] ?: kClass)
        }
}
