package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AoDi

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 화면보다 먼저 실행되는 프로세스 초기화 지점에서 Room 객체를 한 번 준비한다.
        // ::class는 Kotlin의 KClass, .java는 Room이 요구하는 Java Class를 제공한다.
        val database =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()

        // DAO는 Room이 생성하므로 객체를 등록하고, Repository는 구현 클래스 정보만 연결한다.
        // 이후 CartRepository 요청 시 AoDi가 DAO를 재사용해 DefaultCartRepository를 만든다.
        AoDi.register(CartProductDao::class, database.cartProductDao())
        AoDi.registerInterfaceRule(CartRepository::class, DefaultCartRepository::class)
    }
}
