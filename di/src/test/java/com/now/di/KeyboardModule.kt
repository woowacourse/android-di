package com.now.di

import com.now.annotation.Inject
import com.now.annotation.Qualifier

@Qualifier
annotation class Aluminum

@Qualifier
annotation class Plastic

interface Housing

class AluminumHousing : Housing

class PlasticHousing : Housing

@Qualifier
annotation class Blue

@Qualifier
annotation class Red

interface Switch

class BlueSwitch : Switch

class RedSwitch : Switch

class KeyboardModule : Module {
    @Aluminum
    fun aluminumHousing(): Housing {
        return AluminumHousing()
    }

    @Plastic
    fun plasticHousing(): Housing {
        return PlasticHousing()
    }

    @Blue
    fun blueSwitch(): Switch {
        return BlueSwitch()
    }

    @Red
    fun redSwitch(): Switch {
        return RedSwitch()
    }
}

class KeyboardModuleOneInterfaceOneImplement : Module {
    fun aluminumHousing(): Housing {
        return AluminumHousing()
    }
}

class KeyboardModuleOneInterfaceTwoImplementWithOutQualifier : Module {
    fun aluminumHousing(): Housing {
        return AluminumHousing()
    }

    fun plasticHousing(): Housing {
        return PlasticHousing()
    }
}

class KeyboardModuleOneInterfaceTwoImplementWithQualifier : Module {
    @Aluminum
    fun aluminumHousing(): Housing {
        return AluminumHousing()
    }

    @Plastic
    fun plasticHousing(): Housing {
        return PlasticHousing()
    }
}

class KeyboardModuleTwoInterfaceTwoImplementWithQualifier : Module {
    @Aluminum
    fun aluminumHousing(): Housing {
        return AluminumHousing()
    }

    @Plastic
    fun plasticHousing(): Housing {
        return PlasticHousing()
    }

    @Blue
    fun blueSwitch(): Switch {
        return BlueSwitch()
    }

    @Red
    fun redSwitch(): Switch {
        return RedSwitch()
    }
}

class Keyboard(
    @Inject val housing: Housing,
)

class KeyboardWithQualifier1(
    @Aluminum
    @Inject
    val housing: Housing,
)

class KeyboardWithQualifier2(
    @Aluminum
    @Inject
    val housing: Housing,

    @Blue
    @Inject
    val switch: Switch,
)
