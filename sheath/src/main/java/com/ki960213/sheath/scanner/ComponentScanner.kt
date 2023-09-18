package com.ki960213.sheath.scanner

import com.ki960213.sheath.component.SheathComponent

internal interface ComponentScanner {

    fun findAll(): List<SheathComponent>
}
