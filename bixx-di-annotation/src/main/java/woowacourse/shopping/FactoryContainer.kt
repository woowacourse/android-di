package woowacourse.shopping

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class FactoryContainer(
    private val injector: Injector,
    private val dependencyContainer: DependencyContainer,
) {
    private val providerContainer = mutableMapOf<KClass<*>, MutableList<FactoryValue>>()

    // provide 함수의 반환 타입을 key로, KFunction 형태의 provide 함수와 해당 함수가 있는 Factory를 value로 갖는다.
    fun addProvideFactory(factory: Any) {
        factory::class.declaredMemberFunctions.forEach {
            val returnClassType = it.returnType.jvmErasure
            if (providerContainer[returnClassType] == null) {
                providerContainer[returnClassType] = mutableListOf(FactoryValue(it, factory))
                return@forEach
            }
            providerContainer[returnClassType]!!.add(FactoryValue(it, factory))
        }
    }

    // 1. providerContainer에서 반환값이 동일한 FactoryValue 리스트를 찾는다.
    // 2. FactoryValue리스트에서 qualifierTag를 통해 FactoryValue를 찾는다.
    // 3. FactoryValue를 찾으면 함수 파라미터의 인스턴스들을 생성한다.
    // 4. 파라미터 인스턴스들을 넣어 함수를 실행시킨다.
    // 5. Singleton 어노테이션이 provide 함수에 붙어있다면 DependencyContainer에 저장한다.
    // 6. 인스턴스를 반환한다.
    fun getInstance(target: KClass<*>, qualifierTag: String? = null): Any {
        val factoryValue = getFactoryValue(target, qualifierTag)
        val params = factoryValue.function.valueParameters.map { it.getInstance() }
        val instance = requireNotNull(
            factoryValue.function.call(factoryValue.factory, *params.toTypedArray()),
        ) { "$ERROR_PREFIX $PROVIDE_CALL_FAILED" }.apply { injectProperties() }

        storeIfSingleton(factoryValue, instance)
        return instance
    }

    private fun getFactoryValue(target: KClass<*>, qualifierTag: String?): FactoryValue {
        println("asdf target: $target, container: $providerContainer")
        val factoryValues =
            requireNotNull(providerContainer[target]) { "$ERROR_PREFIX $UNREGISTERED_RETURN_TYPE" }
        factoryValues.forEach { factoryValue ->
            if (factoryValue.function.isSameType(target, qualifierTag)) return factoryValue
        }
        throw NoSuchElementException("$ERROR_PREFIX $NO_FUNCTION")
    }

    private fun KParameter.getInstance(): Any {
        val qualifierTag = this.findAnnotation<Qualifier>()?.className
        return injector.inject(this.type.jvmErasure, qualifierTag)
    }

    private fun storeIfSingleton(factoryValue: FactoryValue, instance: Any) {
        if (factoryValue.function.hasAnnotation<Singleton>()) {
            dependencyContainer.addInstance(
                factoryValue.function.returnType.jvmErasure,
                factoryValue.function.annotations,
                instance,
            )
        }
    }

    private fun KFunction<*>.isSameType(
        target: KClass<*>,
        qualifierTag: String?,
    ): Boolean {
        if (qualifierTag == null) return checkReturnType(target)
        return (checkReturnType(target)) && (checkQualifier(qualifierTag))
    }

    private fun KFunction<*>.checkQualifier(qualifierTag: String): Boolean {
        val qualifier = annotations.filterIsInstance<Qualifier>().firstOrNull() ?: return false
        return qualifier.className == qualifierTag
    }

    private fun KFunction<*>.checkReturnType(target: KClass<*>): Boolean {
        return returnType.jvmErasure == target
    }

    private fun Any.injectProperties() {
        val properties = this@injectProperties::class.declaredMemberProperties
        properties.forEach {
            it.findAnnotation<Inject>() ?: return@forEach
            val qualifier = it.findAnnotation<Qualifier>()
            it.isAccessible = true
            this@injectProperties::class.java.getDeclaredField(it.name).apply {
                isAccessible = true
                set(
                    this@injectProperties,
                    injector.inject(it.returnType.jvmErasure, qualifier?.className),
                )
            }
        }
    }

    fun clear() {
        providerContainer.clear()
    }

    companion object {
        private const val ERROR_PREFIX = "[ERROR]"
        private const val NO_FUNCTION = "찾으려는 생성 함수가 없습니다."
        private const val PROVIDE_CALL_FAILED = "생성 함수 호출에 실패했습니다."
        private const val UNREGISTERED_RETURN_TYPE = "등록되지 않은 반환 타입입니다."

        private var Instance: FactoryContainer? = null
        fun getSingletonInstance(
            injector: Injector,
            dependencyContainer: DependencyContainer,
        ): FactoryContainer {
            return Instance ?: synchronized(this) {
                return Instance ?: FactoryContainer(injector, dependencyContainer)
                    .also { Instance = it }
            }
        }
    }
}
