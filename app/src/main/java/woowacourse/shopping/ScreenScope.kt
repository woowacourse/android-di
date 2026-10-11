package woowacourse.shopping

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import smile.di.SmileDi

class ScreenScopeViewModel(
    val scope: SmileDi,
) : ViewModel()

@Composable
fun rememberScreenScope(moduleFactory: (Application) -> Any): SmileDi =
    (
        viewModel(
            factory =
                viewModelFactory {
                    initializer {
                        val app = this[APPLICATION_KEY] as DiApplication
                        ScreenScopeViewModel(
                            scope =
                                app.smileDi.createChild(
                                    moduleFactory(app),
                                    repository = ScopeContainer(),
                                ),
                        )
                    }
                },
        ) as ScreenScopeViewModel
    ).scope
