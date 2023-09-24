package woowacourse.shopping.otterdi

import woowacourse.shopping.otterdi.annotation.Inject
import woowacourse.shopping.otterdi.annotation.Qualifier
import woowacourse.shopping.otterdi.annotation.Singleton
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

    fun getInstance(kParam: KParameter): Any {
        val key = kParam.identifyKey()
        val provider = providers[key] ?: throwDependencyProviderNotFoundError()
        val provideModule = kParam.getProvideModule() ?: throwDependencyProviderNotFoundError()
        var instance: Any? =
            createInstance(provider, provideModule) ?: throwDependencyProviderNotFoundError()

        if (kParam.hasAnnotation<Singleton>()) {
            if (instances[key] == null) {
                instances[key] = instance
            } else {
                instance = instances[key]
            }
        }
        return instance ?: throwDependencyProviderNotFoundError()
    }

    fun getInstance(kCallable: KCallable<*>): Any {
        val key = kCallable.identifyKey()
        val provider = providers[key] ?: throwDependencyProviderNotFoundError()
        val provideModule = kCallable.getProvideModule() ?: throwDependencyProviderNotFoundError()
        var instance: Any? =
            createInstance(provider, provideModule) ?: throwDependencyProviderNotFoundError()

        if (kCallable.hasAnnotation<Singleton>()) {
            if (instances[key] == null) {
                instances[key] = instance
            } else {
                instance = instances[key]
            }
        }
        return instance ?: throwDependencyProviderNotFoundError()
    }

    private fun createInstance(provideFunc: KFunction<*>, receiver: Module): Any? {
        val instances: MutableMap<String, Any?> = mutableMapOf()
        val injectParams = provideFunc.parameters.filter { it.hasAnnotation<Inject>() }
        if (injectParams.isEmpty()) return provideFunc.call(receiver)

        injectParams.forEach { param ->
            val key = param.identifyKey()
            val provider: KFunction<*> = providers[key] ?: throwDependencyProviderNotFoundError()

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

    private fun throwDependencyProviderNotFoundError(): Nothing =
        throw IllegalArgumentException("의존성을 제공할 수 없습니다.")

    companion object {
        val instances: MutableMap<String, Any?> = mutableMapOf()
    }
}
