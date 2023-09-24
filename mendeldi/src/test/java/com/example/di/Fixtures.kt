package com.example.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.di.annotation.FieldInject
import com.example.di.annotation.Qualifier
import com.example.di.application.DiApplication
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import com.example.di.module.ViewModelModule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ViewModel 선언
class FakeViewModel(
    @FakeInMemoryCartRepositoryQualiefier val cartRepository: FakeCartRepository,
) : ViewModel()

class FakeFieldInjectSuccessViewModel : ViewModel() {
    @FieldInject
    lateinit var productRepository: FakeProductRepository
}

class FakeFieldInjectFailedViewModel : ViewModel() {
    @FieldInject
    private lateinit var productRepository: FakeProductRepository
}

class FakeQualifierFailedViewModel(
    private val cartRepository: FakeCartRepository,
) : ViewModel()

class FakeQualifierSuccessViewModel(
    @FakeInMemoryCartRepositoryQualiefier val cartRepository: FakeCartRepository,
) : ViewModel()

// FakeApplication 선언
class FakeApplication : DiApplication(
    FakeApplicationModule::class.java,
    FakeActivityRetainedModule::class.java,
    FakeViewModelModule::class.java,
    FakeActivityModule::class.java,
)

// FakeModule들 선언
class FakeApplicationModule(context: Context) : ApplicationModule(context) {

    @FakeInMemoryCartRepositoryQualiefier
    fun getInMemoryCartRepository(@FakeInMemoryQualifier localDataSource: FakeLocalDataSource): FakeCartRepository {
        return FakeImMemoryCartRepository(FakeInMemoryLocalDataSource())
    }

    @FakeRoomDbCartRepositoryQualifier
    fun getRoomCartRepository(@FakeRoomDbQualifier localDataSource: FakeLocalDataSource): FakeCartRepository {
        return FakeDefaultCartRepository(localDataSource)
    }

    @FakeInMemoryQualifier
    fun getInMemoryLocalDataSource(): FakeLocalDataSource {
        return FakeInMemoryLocalDataSource()
    }

    @FakeRoomDbQualifier
    fun getDefaultLocalDataSource(): FakeLocalDataSource {
        return FakeDefaultLocalDataSource()
    }
}

class FakeActivityRetainedModule(applicationModule: ApplicationModule) :
    ActivityRetainedModule(applicationModule) {
    fun getDateFormatter(): DateFormatter {
        return DateFormatter(applicationContext)
    }
}

class FakeViewModelModule(activityRetainedModule: ActivityRetainedModule) :
    ViewModelModule(activityRetainedModule) {
    fun getProductRepository(): FakeProductRepository {
        return FakeDefaultProductRepository()
    }
}

class FakeActivityModule(activityContext: Context, activityRetainedModule: ActivityRetainedModule) :
    ActivityModule(activityContext, activityRetainedModule) {
    fun getNumberCounter(): NumberCounter {
        return NumberCounter()
    }
}

// Fake 레포지토리 및 모델 클래스 선언
interface FakeCartRepository
class FakeDefaultCartRepository(val localDataSource: FakeLocalDataSource) :
    FakeCartRepository

class FakeImMemoryCartRepository(val localDataSource: FakeLocalDataSource) :
    FakeCartRepository

interface FakeLocalDataSource
class FakeDefaultLocalDataSource : FakeLocalDataSource
class FakeInMemoryLocalDataSource : FakeLocalDataSource

interface FakeProductRepository
class FakeDefaultProductRepository : FakeProductRepository

class DateFormatter(context: Context) {

    private val formatter = SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.KOREA)

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}

class NumberCounter() {
    var n: Int = 1
        private set
}

// Qualifier 어노테이션 선언
@Qualifier
annotation class FakeRoomDbQualifier

@Qualifier
annotation class FakeInMemoryQualifier

@Qualifier
annotation class FakeRoomDbCartRepositoryQualifier

@Qualifier
annotation class FakeInMemoryCartRepositoryQualiefier
