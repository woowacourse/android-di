package woowacourse.shopping.hashdi

import woowacourse.shopping.hashdi.annotation.Inject
import woowacourse.shopping.hashdi.annotation.Qualifier
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

class AppContainer(private val modules: List<Module>) {

    private val appContainer: MutableMap<String, Any?> = mutableMapOf()

    private val provideFunctions: Map<String, KFunction<*>> = modules.flatMap { module ->
        module::class.declaredMemberFunctions.map {
            it.identifyKey() to it
        }
    }.toMap()

    init {
        initContainer()
    }

    fun getInstance(kCallable: KCallable<*>): Any {
        val identifyKey = kCallable.identifyKey()
        return appContainer[identifyKey]
            ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $identifyKey")
    }

    fun getInstance(kParam: KParameter): Any {
        val identifyKey = kParam.identifyKey()
        return appContainer[identifyKey]
            ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $identifyKey")
    }

    private fun initContainer() {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach { kFunc ->
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
        val qualifier = this.qualifier()
        return "${type}${qualifier ?: ""}"
    }

    private fun KParameter.identifyKey(): String {
        val type = this.type
        val qualifier = this.qualifier()
        return "${type}${qualifier ?: ""}"
    }

    private fun KCallable<*>.qualifier(): Annotation? {
        return this.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
    }

    private fun KParameter.qualifier(): Annotation? {
        return this.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
    }
}
