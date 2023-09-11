package com.example.di

import android.content.Context
import com.example.di.application.DiApplication
import com.example.di.module.ApplicationModule

//class FakeApplication : DiApplication(FakeApplicationModule::class.java)
//
//class FakeApplicationModule(context: Context) : ApplicationModule(context) {
//    fun getRoomCartRepository(): CartRepository {
//        return getOrCreateInstance {
//            DefaultCartRepository(InMemoryLocalDataSource())
//        }
//    }
//
//    @RoomDbCartRepository
//    fun getRoomCartRepository(@RoomDb localDataSource: LocalDataSource): CartRepository {
//        return getOrCreateInstance {
//            DefaultCartRepository(localDataSource)
//        }
//    }
//
//    @RoomDb
//    fun getRoomDataSource(): LocalDataSource {
//        return getOrCreateInstance { InMemoryLocalDataSource() }
//    }
//}