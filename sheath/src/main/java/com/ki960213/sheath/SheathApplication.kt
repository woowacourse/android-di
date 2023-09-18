package com.ki960213.sheath

import android.content.Context
import com.ki960213.sheath.component.SheathComponent1
import com.ki960213.sheath.component.SheathComponentFactory
import com.ki960213.sheath.scanner.DefaultComponentScanner1
import com.ki960213.sheath.sorter.sorted

object SheathApplication {

    lateinit var sheathComponentContainer: Set<SheathComponent1>

    fun run(context: Context) {
        val scanner = DefaultComponentScanner1(context)
        val components: List<SheathComponent1> =
            scanner.findAll() + SheathComponentFactory.create(context)

        val sortedComponents = components.sorted()
        sortedComponents.fold(mutableListOf<SheathComponent1>()) { acc, component ->
            component.instantiate(acc)
            acc.add(component)
            acc
        }
        sheathComponentContainer = sortedComponents.toSet()
    }
}
