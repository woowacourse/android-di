package woowacourse.shopping.util.autoDI.dependencyContainer

import woowacourse.shopping.util.autoDI.ViewModelBundle
import woowacourse.shopping.util.autoDI.dependencyContainer.LifeCycleTypes.*
import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.DisposableRegister
import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.SingletonRegister
import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.ViewModelRegister
import kotlin.reflect.KType

object DependencyContainer {
    const val NOT_EXIST_DEPENDENCY_ERROR =
        "search 함수로 검색한 dependency 가 존재하지 않습니다. qulifier 혹은 선언 모듈을 확인하세요"

    internal val singletons: Singletons = Singletons(mutableListOf())
    internal val disposables: Disposables = Disposables(mutableListOf())
//    private val activities: MutableList<LifeCycleType.Activity<*>> = mutableListOf()
//    private val fragments: MutableList<LifeCycleType.Fragment<*>> = mutableListOf()

    val totalLifeCycleTypes: List<LifeCycleTypes> = listOf(singletons, disposables)

    internal val viewModelBundles: ViewModelBundles = ViewModelBundles(mutableListOf())

    internal val singletonRegister: SingletonRegister = SingletonRegister(singletons)
    internal val disposableRegister: DisposableRegister = DisposableRegister(disposables)
    internal val viewModelRegister: ViewModelRegister = ViewModelRegister(viewModelBundles)

    inline fun <reified T : Any> search(qualifier: String?): T {
        when (qualifier.isNullOrEmpty()) {
            true -> {
                totalLifeCycleTypes.forEach {
                    val eachLifeCyclesSearchResult: T? = it.searchWithOutQualifier<T>()
                    if (eachLifeCyclesSearchResult != null) return eachLifeCyclesSearchResult
                }
            }
            false -> {
                totalLifeCycleTypes.forEach {
                    val eachLifeCyclesSearchResult: T? = it.searchWithQualifier<T>(qualifier)
                    if (eachLifeCyclesSearchResult != null) return eachLifeCyclesSearchResult
                }
            }
        }
        throw IllegalStateException(NOT_EXIST_DEPENDENCY_ERROR)
    }

    fun searchViewModel(kType: KType): ViewModelBundle<*> {
        val viewModelBundlesSearchResult: ViewModelBundle<*>? =
            viewModelBundles.search(kType)
        if (viewModelBundlesSearchResult != null) return viewModelBundlesSearchResult
        throw IllegalStateException(NOT_EXIST_DEPENDENCY_ERROR)
    }
}
