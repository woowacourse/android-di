package org.aprilgom.androiddi.fake

import androidx.activity.ComponentActivity
import org.aprilgom.androiddi.activitiyScope

class FakeScopeActivity : ComponentActivity() {
    val scope by activitiyScope("FakeScope")
    val fakeField = scope.get<FakeField>("FakeField")
}
