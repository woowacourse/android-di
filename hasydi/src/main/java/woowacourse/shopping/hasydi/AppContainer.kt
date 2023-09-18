package woowacourse.shopping.hasydi

import woowacourse.shopping.hasydi.annotation.Inject
import woowacourse.shopping.hasydi.annotation.Qualifier
import woowacourse.shopping.hasydi.annotation.Singleton
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

class AppContainer(private val module: Module) {

    private val appContainer: MutableMap<String, Any?> = mutableMapOf()

    private val provideFunctions: Map<String, KFunction<*>> =
        module::class.declaredMemberFunctions.associateBy {
            it.identifyKey()
        }

    private val providerModuleMap: Map<KFunction<*>, Module> =
        module::class.declaredMemberFunctions.associateWith { module }

    init {
        initContainer()
    }

    fun getInstance(kCallable: KCallable<*>): Any? {
        val identifyKey = kCallable.identifyKey()
        return runCatching { getOrCreateInstance(identifyKey) }.getOrNull()
    }

    fun getInstance(kParam: KParameter): Any? {
        val identifyKey = kParam.identifyKey()
        return runCatching { getOrCreateInstance(identifyKey) }.getOrNull()
    }

    private fun getOrCreateInstance(identifyKey: String): Any {
        val containerValue = appContainer[identifyKey]
            ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $identifyKey")
        if (containerValue is KFunction<*>) {
            val module = providerModuleMap[containerValue]
                ?: throw IllegalArgumentException("생성 함수가 없음: $containerValue")
            return createInstance(containerValue, module)
                ?: throw IllegalArgumentException("인스턴스 생성 실패")
        }
        return containerValue
    }

    private fun initContainer() {
        module::class.declaredMemberFunctions.forEach { kFunc ->
            if (!kFunc.hasAnnotation<Singleton>()) {
                appContainer[kFunc.identifyKey()] = kFunc
            } else {
                appContainer[kFunc.identifyKey()] =
                    createInstance(provideFunc = kFunc, receiver = module)
            }
        }
    }

    private fun createInstance(provideFunc: KFunction<*>, receiver: Module): Any? {
        val injectParams = provideFunc.parameters.filter { it.hasAnnotation<Inject>() }

        if (injectParams.isNotEmpty()) {
            injectParams.forEach { param ->
                val key = param.identifyKey()

                if (appContainer[key] == null) {
                    provideFunctions[key]?.let {
                        appContainer[key] = createInstance(it, receiver)
                    }
                }
            }
            val args = injectParams.associateWith { param ->
                appContainer[param.identifyKey()]
            }.toMutableMap()

            args[provideFunc.parameters.first()] = receiver

            return provideFunc.callBy(args)
        }
        return provideFunc.callBy(mapOf(provideFunc.parameters.first() to receiver))
    }

    private fun KCallable<*>.identifyKey(): String {
        val type = this.returnType
        val qualifier = this.annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        return "${type}${qualifier ?: ""}"
    }

    private fun KParameter.identifyKey(): String {
        val type = this.type
        val qualifier = this.annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        return "${type}${qualifier ?: ""}"
    }
}
