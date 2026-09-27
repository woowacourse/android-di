package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository_impl.DefaultCartRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}

class AppContainer(context: Context) {
    val database: ShoppingDatabase = Room.databaseBuilder<ShoppingDatabase>(context, "db_name")
        .setDriver(AndroidSQLiteDriver())
        .build()
    val cartProductDao: CartProductDao = database.cartProductDao()

    val bindings: Map<KClass<*>, KClass<*>> = mapOf(CartRepository::class to DefaultCartRepository::class)

    val productRepository: ProductRepository = ProductRepository()
}

object SamDi {
    fun resolve(
        modelClass: KClass<*>,
        context: Context
    ): Any {
        val app = context.applicationContext as MyApplication
        val container = app.appContainer // AppContainer 객체 들고오기

        val property = container::class.memberProperties.find {
            it.returnType.classifier == modelClass
        }
        if(property != null) {
            return property.call(container)!!
        }

        val implType = container.bindings[modelClass] ?: modelClass
        val constructor = implType.primaryConstructor!! // 생성자 확인
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }

        val inst =
            types.map { type ->
                resolve(type, context)
            }


        return constructor.call(*inst.toTypedArray())
    }

    fun viewModelFactory(
        context: Context
    ): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = resolve(
                modelClass.kotlin,
                context,
            ) as T
        }
}
