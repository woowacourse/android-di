package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.RepositoryContainer

class ShoppingApplication : Application() {
    val repositoryContainer = RepositoryContainer()
}
