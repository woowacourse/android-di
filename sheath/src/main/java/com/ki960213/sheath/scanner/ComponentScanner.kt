package com.ki960213.sheath.scanner

import com.ki960213.sheath.component.SheathComponent

interface ComponentScanner {

    fun findAll(): List<SheathComponent>
}
