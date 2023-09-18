package com.ki960213.sheath

import android.content.Context
import com.ki960213.sheath.component.SheathComponent
import com.ki960213.sheath.component.SheathComponentFactory
import com.ki960213.sheath.scanner.DefaultComponentScanner
import com.ki960213.sheath.sorter.sorted

object SheathApplication {

    lateinit var sheathComponentContainer: Set<SheathComponent>

    fun run(context: Context) {
        val scanner = DefaultComponentScanner(context)
        val components: List<SheathComponent> =
            scanner.findAll() + SheathComponentFactory.create(context)

        val sortedComponents = components.sorted()
        sortedComponents.fold(mutableListOf<SheathComponent>()) { acc, component ->
            component.instantiate(acc)
            acc.add(component)
            acc
        }
        sheathComponentContainer = sortedComponents.toSet()
    }
}
