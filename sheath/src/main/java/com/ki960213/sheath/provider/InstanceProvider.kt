package com.ki960213.sheath.provider

import com.ki960213.sheath.component.SheathComponent
import com.ki960213.sheath.sorter.sorted

object InstanceProvider {

    private val container: MutableMap<SheathComponent, Any> = mutableMapOf()

    fun addComponents(components: List<SheathComponent>) {
        val sortedComponents = components.sorted()
        sortedComponents.forEach {
            container[it] = it.instantiated(container.values.toList())
        }
    }

    fun get(sheathComponent: SheathComponent): Any {
        if (sheathComponent.isSingleton) {
            return container[sheathComponent]
                ?: throw IllegalArgumentException("${sheathComponent.name} 컴포넌트는 등록되지 않았습니다.")
        }
        return sheathComponent.instantiated(container.values.toList())
    }
}
