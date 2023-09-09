package woowacourse.shopping.di.module

import android.content.Context

// 액티비티 모듈은 자신보다 상위 수준에서 관리되는 애플리케이션 모듈을 참조해서 알아야 한다.
// 나중에 애노테이션 사용이 가능해진다면, 특정 액티비티는 어떤 액티비티 모듈을 사용할지도 지정해줄 수 있게 할 예정입니다.
// 현재는 자신이 사용할 액티비티 모듈을 생성자로부터 타입을 받는 형태입니다.
abstract class ActivityModule(activityContext: Context, applicationModule: ApplicationModule) :
    Module(activityContext, applicationModule)
