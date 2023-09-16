package woowacourse.shopping.otterdi

import woowacourse.shopping.otterdi.annotation.Inject
import woowacourse.shopping.otterdi.annotation.Qualifier
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

class Dependencies(private val modules: List<Module>) {
    constructor(vararg modules: Module) : this(modules.toList())

    private val providers: MutableMap<String, KFunction<*>> = mutableMapOf()
    private val instances: MutableMap<String, Any?> = mutableMapOf()

    init {
        initProviders()
        initInstances()
    }

    private fun initProviders() {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach {
                providers[it.identifyKey()] = it
            }
        }
    }

    private fun initInstances() {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach {
                instances[it.identifyKey()] = createInstance(it, module)
            }
        }
    }

    private fun createInstance(provideFunc: KFunction<*>, receiver: Module): Any? {
        val injectParams = provideFunc.parameters.filter { it.hasAnnotation<Inject>() }
        if (injectParams.isEmpty()) return provideFunc.call(receiver)

        injectParams.forEach { param ->
            val key = param.identifyKey()
            providers[key]?.let { provider ->
                if (instances[key] == null) instances[key] = createInstance(provider, receiver)
            } ?: throw IllegalArgumentException("필요한 의존성을 찾을 수 없습니다. ($param)")
        }

        val args = injectParams.associateWith { instances[it.identifyKey()] }.toMutableMap()
        args[provideFunc.parameters.first()] = receiver
        return provideFunc.callBy(args)
    }

    fun getInstances(params: List<KParameter>): List<Any> = params.map { getInstance(it) }

    fun getInstance(kParam: KParameter): Any = instances[kParam.identifyKey()]
        ?: throw IllegalArgumentException("${kParam}에 대한 의존성을 가져오는데 실피하였습니다.")

    fun getInstance(kCallable: KCallable<*>): Any = instances[kCallable.identifyKey()]
        ?: throw IllegalArgumentException("${kCallable}에 대한 의존성을 가져오는데 실피하였습니다.")

    private fun KCallable<*>.identifyKey(): String =
        "$returnType:${qualifier()?.name ?: ""}"

    private fun KParameter.identifyKey(): String = "$type:${qualifier()?.name ?: ""}"

    private fun KCallable<*>.qualifier(): Qualifier? =
        this.annotations.find { it is Qualifier } as Qualifier?

    private fun KParameter.qualifier(): Qualifier? =
        this.annotations.find { it is Qualifier } as Qualifier?
}
