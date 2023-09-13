package com.ki960213.sheath

import android.content.Context
import com.ki960213.sheath.component.SheathComponent
import com.ki960213.sheath.component.SheathComponentByContext
import com.ki960213.sheath.provider.InstanceProvider
import com.ki960213.sheath.scanner.DefaultComponentScanner

object SheathApplication1 {

    fun run(context: Context) {
        val scanner = DefaultComponentScanner(context)
        val components: List<SheathComponent> =
            scanner.findAll() + SheathComponentByContext(context)
        InstanceProvider.addComponents(components)
    }
}
