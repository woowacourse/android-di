package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.di.DataContainer
import woowacourse.shopping.data.di.RepositoryContainer
import woowacourse.shopping.ui.di.CommonViewModelFactory

class ShoppingApplication: Application() {
    private lateinit var dataContainer: DataContainer
    private lateinit var repositoryContainer: RepositoryContainer
    lateinit var viewModelFactory: CommonViewModelFactory

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping_db"
        ).build()

        dataContainer = DataContainer.create(database)
        repositoryContainer = RepositoryContainer(dataContainer)
        viewModelFactory = CommonViewModelFactory(repositoryContainer)
    }
}
