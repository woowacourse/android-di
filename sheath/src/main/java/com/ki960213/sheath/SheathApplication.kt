package com.ki960213.sheath

import android.app.Application
import com.ki960213.sheath.component.Component
import com.ki960213.sheath.container.SingletonContainer
import com.ki960213.sheath.instantiater.instantiateByPrimaryConstructor
import com.ki960213.sheath.scanner.ClassScanner
import com.ki960213.sheath.scanner.DefaultClassScanner
import com.ki960213.sheath.sorter.sortedTopologically
import kotlin.reflect.KClass

abstract class SheathApplication(
    scanner: ClassScanner? = null,
) : Application(), SingletonContainer {
    private lateinit var container: List<Any>

    private val mScanner: ClassScanner by lazy {
        scanner ?: DefaultClassScanner(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        setupContainer()
    }

    private fun setupContainer() {
        val classes = mScanner.findAll(Component::class.java)

        val sortedClasses = classes.sortedTopologically()

        container = sortedClasses.fold(mutableListOf()) { instances, clazz ->
            instances.add(clazz.instantiateByPrimaryConstructor(instances))
            instances
        }
    }

    override fun getInstance(clazz: KClass<*>): Any {
        return container.find { element -> clazz.isInstance(element) }
            ?: throw IllegalStateException("의존성 검사 로직이 잘못되었습니다. 확인해주세요.")
    }
}
