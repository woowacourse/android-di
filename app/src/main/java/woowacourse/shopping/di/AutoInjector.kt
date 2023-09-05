package woowacourse.shopping.di

import woowacourse.shopping.di.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

class AutoInjector(
    private val module: Module,
) : Injector {
    override fun <T : Any> inject(modelClass: Class<T>): T {
        // 모듈이 가진 자동 주입 생성 함수들 중, 일치하는 반환타입을 가진 함수 조사
        val injectableFunctionWithModuleMap = searchInjectableFunctions(listOf(module), modelClass)

        // 해당 객체를 만들기 위한 필요 구성 요소들을 얻는 과정.
        return when (injectableFunctionWithModuleMap.size) {
            0 -> createWithPrimaryConstructor(modelClass) // 모듈에 정의된 메소드들 중 해당되는 것이 없다는 것은, 이 객체를 만드는데 주생성자면 충분하다는 의미.
            else -> { // 추후 1과 else로 분리 예정
                val injectFunction =
                    injectableFunctionWithModuleMap.keys.first() // 메소드들 중 어노테이션으로 판단해서 일치하는 메소드를 실행시켜서 반환시킬 것임.
                val injectModule = injectableFunctionWithModuleMap[injectFunction]
                    ?: throw NullPointerException("${injectFunction.name} 함수가 없습니다.")
                createWithModuleFunc(injectModule, injectFunction)
            }
        }
    }

    private fun <T : Any> searchInjectableFunctions(
        modules: List<Module>,
        modelClass: Class<T>,
    ): Map<KFunction<*>, Module> {
        return mutableMapOf<KFunction<*>, Module>().apply {
            modules.forEach { module ->
                val matchFunctionModuleMaps = module.getPublicMethodMap().filter { entry ->
                    val returnKClass = entry.key.returnType.classifier as KClass<*>
                    modelClass.kotlin.isSubclassOf(returnKClass)
                }
                putAll(matchFunctionModuleMaps)
            }
            // 만약 현재 트리 레벨의 모듈들에서 해당 메소드가 없다면, 부모 모듈 검사
            if (isEmpty()) putAll(searchInjectableFunctionsFromParents(modules, modelClass))
        }
    }

    private fun <T : Any> searchInjectableFunctionsFromParents(
        childModules: List<Module>,
        modelClass: Class<T>,
    ): Map<KFunction<*>, Module> {
        return mutableMapOf<KFunction<*>, Module>().apply {
            val parentModules: List<Module> =
                childModules.filter { it.parentModule != null }.map { it.parentModule!! }
            if (parentModules.isEmpty().not()) {
                putAll(searchInjectableFunctions(parentModules, modelClass))
            }
        }
    }

    private fun <T : Any> createWithModuleFunc(module: Module, func: KFunction<*>): T {
        val args = getArguments(func).toMutableList()
        args.add(0, module) // 수신 객체 지정.
        @Suppress("UNCHECKED_CAST")
        return func.call(*args.toTypedArray()) as T
    }

    private fun <T : Any> createWithPrimaryConstructor(modelClass: Class<T>): T {
        val primaryConstructor =
            modelClass.kotlin.primaryConstructor
                ?: throw NullPointerException("모듈에 특정 클래스를 주 생성자로 인스턴스화 하는데 필요한 인자를 제공하는 함수를 정의하지 않았습니다")
        val args = getArguments(primaryConstructor)
        return primaryConstructor.call(*args.toTypedArray())
    }

    private fun getArguments(func: KFunction<*>): List<Any> {
        return func.valueParameters.map { param ->
            val paramKClass = param.type.classifier as KClass<*>
            inject(paramKClass.java)
        }
    }
}
