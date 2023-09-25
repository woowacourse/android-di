package com.ki960213.sheath

import android.content.Context
import com.ki960213.sheath.component.ContextSheathComponent
import com.ki960213.sheath.component.SheathComponent
import com.ki960213.sheath.scanner.ComponentScanner
import com.ki960213.sheath.sorter.sorted
import kotlin.reflect.KType

object SheathApplication {

    lateinit var sheathComponentContainer: SheathComponentContainer

    fun run(context: Context) {
        val scanner = ComponentScanner(context)
        val components: List<SheathComponent> =
            scanner.findAll() + ContextSheathComponent(context)

        initContainer(components)
    }

    private fun initContainer(components: List<SheathComponent>) {
        val container: Map<KType, SheathComponent> = components.sorted()
            .fold(mutableMapOf()) { acc, component ->
                component.instantiate(acc.values.toList())
                acc[component.type] = component
                acc
            }

        sheathComponentContainer = SheathComponentContainer(container)
    }
}
