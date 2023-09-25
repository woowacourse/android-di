package com.ssu.androidi.container

import android.content.Context
import com.ssu.androidi.activity.DiActivity
import com.ssu.androidi.application.DiApplication
import com.ssu.di.annotation.Injected
import com.ssu.di.annotation.Qualifier
import com.ssu.di.module.Module
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class DefaultContainerTest {
    @Test
    fun `Activity를 종료하고 다시 생성했을 때, 싱글톤은 동일한 인스턴스가 주입되고 싱글톤이 아니면 새로운 인스턴스가 주입된다`() {

        // GIVEN
        var firstAppleRepository: FakeRepository? = null
        var firstPeachRepository: FakeRepository? = null
        var secondAppleRepository: FakeRepository? = null
        var secondPeachRepository: FakeRepository? = null

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java).setup()

        firstAppleRepository = activity.get().appleRepository
        firstPeachRepository = activity.get().peachRepository

        // WHEN : 액티비티 destroy 후 다시 생성
        val recreatedActivity = activity.recreate()
        secondAppleRepository = recreatedActivity.get().appleRepository
        secondPeachRepository = recreatedActivity.get().peachRepository

        // THEN : instance 비교
        assert(firstAppleRepository === secondAppleRepository)
        assert(firstPeachRepository !== secondPeachRepository)
    }
}


class FakeApplication : DiApplication(FakeApplicationModule::class) {
    override fun onCreate() {
        super.onCreate()
        setTheme(androidx.appcompat.R.style.Theme_AppCompat)
    }
}

class FakeApplicationModule(context: Context) : Module {
    @Qualifier("APPLE")
    fun provideAppleRepository() = FakeAppleRepository()
}

class FakeActivityModule(context: Context) : Module {
    @Qualifier("PEACH")
    fun providePeachRepository() = FakePeachRepository()
}

class FakeActivity : DiActivity(FakeActivityModule::class) {

    @Injected
    @Qualifier("APPLE")
    lateinit var appleRepository: FakeRepository

    @Injected
    @Qualifier("PEACH")
    lateinit var peachRepository: FakeRepository
}

interface FakeRepository
class FakeAppleRepository : FakeRepository
class FakePeachRepository : FakeRepository
