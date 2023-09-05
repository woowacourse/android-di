package com.ki960213.sheath

import android.app.Application
import com.ki960213.sheath.component.Component
import com.ki960213.sheath.container.SingletonContainer
import com.ki960213.sheath.scanner.ClassScanner
import com.ki960213.sheath.scanner.DefaultClassScanner
import com.ki960213.sheath.util.ClassesTopologicalSorter
import com.ki960213.sheath.util.InstanceGenerator
import kotlin.reflect.KClass

abstract class SheathApplication(
    scanner: ClassScanner? = null,
) : Application(), SingletonContainer {
    private val container: MutableList<Any> = mutableListOf()

    private val mScanner: ClassScanner by lazy {
        scanner ?: DefaultClassScanner(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        setupContainer()
    }

    private fun setupContainer() {
        val classes = mScanner.findAll(Component::class.java)

        val sortedClasses = ClassesTopologicalSorter.sort(classes)

        sortedClasses.forEach { container.add(InstanceGenerator.generate(it, container.toList())) }
    }

    override fun getInstance(clazz: KClass<*>): Any {
        val instances = container.filter { element -> clazz.isInstance(element) }
        when {
            instances.isEmpty() -> throw IllegalStateException("Application container에 ${clazz.qualifiedName} 타입의 인스턴스가 존재하지 않습니다.")
            instances.size >= 2 -> throw IllegalStateException("Application container에 ${clazz.qualifiedName} 타입의 인스턴스가 두 개 이상 존재합니다.")
        }
        return instances[0]
    }
}
