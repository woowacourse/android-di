package woowacourse.shopping.di

import com.m6z1.moongdi.annotation.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemory

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomDB
