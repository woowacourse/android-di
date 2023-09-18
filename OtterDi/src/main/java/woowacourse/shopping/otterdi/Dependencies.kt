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

    private val providers: MutableMap<String, KFunction<*>> =
        mutableMapOf<String, KFunction<*>>().apply {
            modules.forEach { module ->
                module::class.declaredMemberFunctions.forEach {
                    this[it.identifyKey()] = it
                }
            }
        }

    fun getInstances(params: List<KParameter>): List<Any> = params.map { getInstance(it) }

    fun getInstance(kParam: KParameter): Any = createInstance(
        providers[kParam.identifyKey()] ?: throwError(),
        kParam.getProvideModule() ?: throwError(),
    ) ?: throwError()

    fun getInstance(kCallable: KCallable<*>): Any = createInstance(
        providers[kCallable.identifyKey()] ?: throwError(),
        kCallable.getProvideModule() ?: throwError(),
    ) ?: throwError()

    private fun createInstance(provideFunc: KFunction<*>, receiver: Module): Any? {
        val instances: MutableMap<String, Any?> = mutableMapOf() // 매번 필요한 인스턴스를 만들어 사용하도록 함
        val injectParams = provideFunc.parameters.filter { it.hasAnnotation<Inject>() }
        if (injectParams.isEmpty()) return provideFunc.call(receiver)

        injectParams.forEach { param ->
            val key = param.identifyKey()
            val provider: KFunction<*> = providers[key] ?: throwError()

            if (instances[key] == null) instances[key] = createInstance(provider, receiver)
        }

        val args = injectParams.associateWith { instances[it.identifyKey()] }.toMutableMap()
        args[provideFunc.parameters.first()] = receiver
        return provideFunc.callBy(args)
    }

    private fun KCallable<*>.identifyKey(): String =
        "$returnType:${qualifier()?.name ?: ""}"

    private fun KParameter.identifyKey(): String = "$type:${qualifier()?.name ?: ""}"

    private fun KCallable<*>.qualifier(): Qualifier? =
        this.annotations.find { it is Qualifier } as Qualifier?

    private fun KParameter.qualifier(): Qualifier? =
        this.annotations.find { it is Qualifier } as Qualifier?

    private fun KParameter.getProvideModule(): Module? =
        modules.find { module: Module ->
            module::class.declaredMemberFunctions.find { func ->
                func.identifyKey() == this.identifyKey()
            } != null
        }

    private fun KCallable<*>.getProvideModule(): Module? =
        modules.find { module: Module ->
            module::class.declaredMemberFunctions.find { func ->
                func.identifyKey() == this.identifyKey()
            } != null
        }

    private fun throwError(): Nothing = throw IllegalArgumentException("의존성을 제공할 수 없습니다.")
}
