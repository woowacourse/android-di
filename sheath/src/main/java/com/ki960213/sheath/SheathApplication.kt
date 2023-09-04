package com.ki960213.sheath

import android.app.Application
import com.ki960213.sheath.component.Component
import com.ki960213.sheath.scanner.ClassScanner
import com.ki960213.sheath.util.ClassesTopologicalSorter
import com.ki960213.sheath.util.InstanceGenerator

abstract class SheathApplication : Application() {
    private val _container: MutableList<Any> = mutableListOf()
    val container get() = _container.toList()

    override fun onCreate() {
        super.onCreate()

        setupContainer()
    }

    private fun setupContainer() {
        val scanner = ClassScanner(applicationContext)
        val classes = scanner.findAll(Component::class.java)

        val sortedClasses = ClassesTopologicalSorter.sort(classes)

        _container.addAll(sortedClasses.map { InstanceGenerator.generate(it, _container.toList()) })
    }
}
