package woowacourse.shopping.data

import com.example.di.ActivityLifespan
import com.example.di.Dependency
import com.example.di.Module

class FakeDatabaseModule : Module {
    @Dependency
    @ActivityLifespan
    fun cartProductDao(): CartProductDao = FakeCartProductDao()
}
