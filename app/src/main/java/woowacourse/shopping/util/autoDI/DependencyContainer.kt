package woowacourse.shopping.util.autoDI

import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.DisposableRegister
import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.SingletonRegister
import kotlin.reflect.typeOf

object DependencyContainer {
    const val NOT_EXIST_DEPENDENCY_ERROR =
        "search 함수로 검색한 dependency 가 존재하지 않습니다. qulifier 혹은 선언 모듈을 확인하세요"

    internal val singletons: MutableList<LifeCycleType.Singleton<*>> = mutableListOf()
    internal val disposables: MutableList<LifeCycleType.Disposable<*>> = mutableListOf()
//    private val activities: MutableList<LifeCycleType.Activity<*>> = mutableListOf()
//    private val fragments: MutableList<LifeCycleType.Fragment<*>> = mutableListOf()

    val test: List<List<LifeCycleType<*>>> = listOf(singletons, disposables)

    internal val singletonRegister: SingletonRegister = SingletonRegister(singletons)
    internal val disposableRegister: DisposableRegister = DisposableRegister(disposables)

    inline fun <reified T : Any> search(qualifier: String?): T {
        val type = typeOf<T>()
        when (qualifier.isNullOrEmpty()) {
            true -> {
                test.forEach {
                    it.forEach {
                        if (it.type == type) return it.getInstance() as T
                    }
                }
            }
            false -> {
                test.forEach {
                    it.forEach {
                        if (it.type == type && it.qualifier == qualifier) return it.getInstance() as T
                    }
                }
            }
        }
        throw IllegalStateException(NOT_EXIST_DEPENDENCY_ERROR)
    }
}
