package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.data.di.RepositoryModule

class AppContainer(context: Context) {
    val repositoryModule = RepositoryModule()
}
