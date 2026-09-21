package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.di.RepositoryContainer
import woowacourse.shopping.ui.di.CommonViewModelFactory

class ShoppingApplication: Application() {
    private val repositoryContainer = RepositoryContainer()
    val viewModelFactory = CommonViewModelFactory(repositoryContainer)
}
