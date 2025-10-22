package woowacourse.shopping.android.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel

sealed interface Scope {
    object ApplicationScope : Scope

    data class ViewModelScope(
        val owner: ViewModel,
    ) : Scope

    data class ActivityScope(
        val owner: ComponentActivity,
        val retained: Boolean = false,
    ) : Scope
}
