package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class AppContainerStore(
    appContainer: AppContainer,
) {
    private val cache: MutableMap<KClass<*>, Any> = mutableMapOf()

    operator fun get(clazz: KClass<*>): Any? = (cache[clazz] as? Lazy<*>)?.value ?: cache[clazz]

    init {
        appContainer::class.memberProperties.forEach { property ->
            property.isAccessible = true
            val editableProperty = property as? KProperty1<AppContainer, *>
            cache[property.returnType.jvmErasure] =
                editableProperty?.getDelegate(appContainer) ?: editableProperty?.get(appContainer)
                    ?: throw IllegalStateException(
                        "$ERR_PROPERTY_NOT_INITIALIZED : ${property.returnType}",
                    )
        }
    }

    fun instantiate(clazz: KClass<*>): Any? {
        val inProgress = mutableSetOf<KClass<*>>()
        return instantiate(clazz, inProgress)
    }

    private fun instantiate(
        clazz: KClass<*>,
        inProgress: MutableSet<KClass<*>>,
    ): Any? {
        if (cache.containsKey(clazz)) return this[clazz]

        if (inProgress.contains(clazz)) {
            throw IllegalArgumentException(
                "$ERR_CIRCULAR_DEPENDENCY_DETECTED : ${clazz.simpleName}",
            )
        }

        return clazz.primaryConstructor?.let { constructor ->
            inProgress.add(clazz)
            cache[clazz] = reflect(constructor, inProgress)
            inProgress.remove(clazz)
            cache[clazz]
        } ?: throw IllegalArgumentException("$ERR_CONSTRUCTOR_NOT_FOUND : ${clazz.simpleName}")
    }

    private fun reflect(
        constructor: KFunction<Any>,
        inProgress: MutableSet<KClass<*>>,
    ): Any {
        val arguments =
            constructor.parameters
                .filter { !it.isOptional }
                .associateWith { parameter ->
                    val instance = instantiate(parameter.type.jvmErasure, inProgress)
                    instance
                }
        return constructor.callBy(arguments)
    }

    companion object {
        private const val ERR_PROPERTY_NOT_INITIALIZED = "프로퍼티가 초기화되지 않았습니다"
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
        private const val ERR_CONSTRUCTOR_NOT_FOUND =
            "주 생성자를 찾을 수 없습니다. 인터페이스, 추상 클래스의 경우 AppContainer에 구현체를 등록해주세요"
    }
}

fun CreationExtras.containerProvider(clazz: KClass<*>): Any? {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainerStore
    return store.instantiate(clazz)
}
