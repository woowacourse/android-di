//package com.example.di
//
//import android.content.Context
//import com.example.di.annotation.Qualifier
//import com.example.di.application.DiApplication
//import com.example.di.module.ApplicationModule
//
//class FakeApplication : DiApplication(FakeApplicationModule::class.java)
//
//class FakeApplicationModule(context: Context) : ApplicationModule(context) {
//
//    @FakeInMemoryCartRepositoryQualiefier
//    fun getInMemoryCartRepository(@FakeInMemoryQualifier localDataSource: FakeLocalDataSource): FakeCartRepository {
//        return FakeImMemoryCartRepository(FakeInMemoryLocalDataSource())
//    }
//
//    @FakeRoomDbCartRepositoryQualifier
//    fun getRoomCartRepository(@FakeRoomDbQualifier localDataSource: FakeLocalDataSource): FakeCartRepository {
//        return FakeDefaultCartRepository(localDataSource)
//    }
//
//    @FakeInMemoryQualifier
//    fun getInMemoryLocalDataSource(): FakeLocalDataSource {
//        return FakeInMemoryLocalDataSource()
//    }
//
//    @FakeRoomDbQualifier
//    fun getDefaultLocalDataSource(): FakeLocalDataSource {
//        return FakeDefaultLocalDataSource()
//    }
//}
//
//interface FakeCartRepository
//class FakeDefaultCartRepository(val localDataSource: FakeLocalDataSource) :
//    FakeCartRepository
//
//class FakeImMemoryCartRepository(val localDataSource: FakeLocalDataSource) :
//    FakeCartRepository
//
//interface FakeLocalDataSource
//class FakeDefaultLocalDataSource : FakeLocalDataSource
//class FakeInMemoryLocalDataSource : FakeLocalDataSource
//
//interface FakeProductRepository
//class FakeDefaultProductRepository : FakeProductRepository
//
//@Qualifier
//annotation class FakeRoomDbQualifier
//
//@Qualifier
//annotation class FakeInMemoryQualifier
//
//@Qualifier
//annotation class FakeRoomDbCartRepositoryQualifier
//
//@Qualifier
//annotation class FakeInMemoryCartRepositoryQualiefier
