package woowacourse.shopping.di

import com.m6z1.moongdi.annotation.Qualifier

@Qualifier("InMemory")
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemory

@Qualifier("RoomDB")
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomDB
