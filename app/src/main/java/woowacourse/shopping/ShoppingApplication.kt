package woowacourse.shopping

import com.di.woogidi.application.DiApplication
import woowacourse.shopping.di.module.ShoppingApplicationModule

class ShoppingApplication : DiApplication() {

    override fun onCreate() {
        super.onCreate()

        /**
         * todo: module에 특정 인스턴스를 생성하는 함수(방법)를 추가할 수 있는 방향으로 개선 및 dsl을 적용하면 더욱 좋은 di 라이브러리가 될 듯하다.
         */
        injector.applicationModule = ShoppingApplicationModule(this)
    }
}
