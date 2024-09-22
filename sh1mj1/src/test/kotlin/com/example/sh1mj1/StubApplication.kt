package com.example.sh1mj1.com.example.sh1mj1

import android.app.Application
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.DefaultAppContainer

class StubApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}

/*
class ShoppingApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        container.add(
            singletonComponent<CartProductDao>(CartProductDao.instance(this), Qualifier("RoomDao", generate = true)),
        )

        container.add(
            singletonComponent<ProductRepository>(InMemoryProductRepository(), Qualifier("InMemory")),
            singletonComponent<CartRepository>(InMemoryCartRepository(), Qualifier("InMemory")),
            singletonComponent<CartRepository>(
                DefaultCartRepository(),
                Qualifier("RoomDao"),
            ),
        )
    }
}
 */
