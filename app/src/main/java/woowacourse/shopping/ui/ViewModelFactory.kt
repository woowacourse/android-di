package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.DependencyProvider
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val constructor: KFunction<VM> =
            modelClass.kotlin.primaryConstructor ?: error("${modelClass.name}의 주 생성자를 찾을 수 없습니다.")
        val parameters: Array<Any> =
            constructor.parameters
                .map { parameter: KParameter ->
                    DependencyProvider.dependency(parameter.type)
                }.toTypedArray()
        return constructor.call(*parameters)
    }
}
