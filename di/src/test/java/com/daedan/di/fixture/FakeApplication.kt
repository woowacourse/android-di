package com.daedan.di.fixture

import android.app.Application
import com.daedan.di.AppContainerStore
import com.daedan.di.DiComponent

class FakeApplication :
    Application(),
    DiComponent {
    override val appContainerStore = AppContainerStore()
}
