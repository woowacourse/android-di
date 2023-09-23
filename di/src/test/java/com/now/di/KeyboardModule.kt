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
    fun provideAluminumHousing(): Housing {
        return AluminumHousing()
    }

    @Plastic
    fun providePlasticHousing(): Housing {
        return PlasticHousing()
    }

    @Blue
    fun provideBlueSwitch(): Switch {
        return BlueSwitch()
    }

    @Red
    fun provideRedSwitch(): Switch {
        return RedSwitch()
    }
}

class 하나의_인터페이스에_대해_하나의_구현체가_있는_모듈 : Module {
    fun provideAluminumHousing(): Housing {
        return AluminumHousing()
    }
}

class 하나의_인터페이스에_대해_두_개의_구현체가_Qualifier로_구분되지_않는_모듈 : Module {
    fun provideAluminumHousing(): Housing {
        return AluminumHousing()
    }

    fun providePlasticHousing(): Housing {
        return PlasticHousing()
    }
}

class 하나의_인터페이스에_대해_두_개의_구현체가_Qualifier로_구분되는_모듈 : Module {
    @Aluminum
    fun provideAluminumHousing(): Housing {
        return AluminumHousing()
    }

    @Plastic
    fun providePlasticHousing(): Housing {
        return PlasticHousing()
    }
}

class 두_개의_인터페이스에_대해_두_개의_구현체가_Qualifier로_구분되는_모듈 : Module {
    @Aluminum
    fun provideAluminumHousing(): Housing {
        return AluminumHousing()
    }

    @Plastic
    fun providePlasticHousing(): Housing {
        return PlasticHousing()
    }

    @Blue
    fun provideBlueSwitch(): Switch {
        return BlueSwitch()
    }

    @Red
    fun provideRedSwitch(): Switch {
        return RedSwitch()
    }
}

class 주입받을_인자가_한_개_있는_키보드_클래스(
    @Inject val housing: Housing,
)

class Qualifier가_한_개_있는_키보드_클래스(
    @Aluminum
    @Inject
    val housing: Housing,
)

class Qualifier가_두_개_있는_키보드_클래스(
    @Aluminum
    @Inject
    val housing: Housing,

    @Blue
    @Inject
    val switch: Switch,
)

class KeyCap

class Keyboard(
    val keyCap: KeyCap,
)

class 재귀적_호출이_필요한_모듈 : Module {
    fun provideKeyCap(): KeyCap {
        return KeyCap()
    }

    fun provideKeyboard(keyCap: KeyCap): Keyboard {
        return Keyboard(keyCap)
    }
}
