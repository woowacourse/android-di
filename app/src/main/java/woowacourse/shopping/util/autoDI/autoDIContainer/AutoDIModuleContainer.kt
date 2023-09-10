package woowacourse.shopping.util.autoDI.autoDIContainer

import woowacourse.shopping.util.autoDI.ViewModelBundle
import kotlin.reflect.KType

object AutoDIModuleContainer {

    const val NOT_EXIST_DEPENDENCY_ERROR =
        "search 함수로 검색한 dependency 가 존재하지 않습니다. qulifier 혹은 선언 모듈을 확인하세요"

    private val autoDIModules: AutoDIModules = AutoDIModules(mutableListOf())

    internal fun <T : Any> searchLifeCycleType(kType: KType, qualifier: String?): T =
        autoDIModules.searchLifeCycleType(kType, qualifier) ?: throw IllegalStateException(
            NOT_EXIST_DEPENDENCY_ERROR,
        )

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*> =
        autoDIModules.searchViewModelBundle(kType) ?: throw IllegalStateException(
            NOT_EXIST_DEPENDENCY_ERROR,
        )
}
