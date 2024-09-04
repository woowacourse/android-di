package woowacourse.shopping.data.di.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun inject(): ViewModelProvider.Factory = ViewModelComponent()

private class ViewModelComponent : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR.format(modelClass))

        val parameters =
            constructor.parameterTypes.map { bindClassType ->
                DiSingletonComponent.match(bindClassType)
            }.toTypedArray()

        return constructor.newInstance(*parameters) as T
    }

    companion object {
        private const val ERROR_CONSTRUCTOR = "No suitable constructor for %s"
    }
}
