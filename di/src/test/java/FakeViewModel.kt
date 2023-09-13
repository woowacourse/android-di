import com.hyegyeong.di.Inject
import com.hyegyeong.di.RoomDB

class FakeViewModel(@Inject @RoomDB val fakeRepository: FakeRepository)