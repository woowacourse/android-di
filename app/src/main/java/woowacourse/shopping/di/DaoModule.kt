package woowacourse.shopping.di

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import com.woowa.di.injection.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

class DaoModule private constructor(context: Context) :
    Module<DaoModule, DaoDI>,
    DefaultLifecycleObserver {
        private val database: ShoppingDatabase =
            Room.databaseBuilder(context, ShoppingDatabase::class.java, "shopping").build()
        private lateinit var daoMap: Map<String, DaoDI>
        private lateinit var daoBinder: DaoBinder

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            daoBinder = DaoBinder(database)
            daoMap = createRepositoryMap(daoBinder)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            instance = null
            owner.lifecycle.removeObserver(this)
        }

        private fun createRepositoryMap(daoBinder: DaoBinder): Map<String, DaoDI> {
            return DaoBinder::class.declaredMemberFunctions
                .filter { it.returnType.jvmErasure.isSubclassOf(DaoDI::class) }
                .associate { kFunction ->
                    val result = kFunction.call(daoBinder) as DaoDI
                    val key =
                        kFunction.returnType.jvmErasure.simpleName
                            ?: error("$result 의 key값을 지정할 수 없습니다.")
                    key to result
                }
        }

        override fun getDIInstance(type: KClass<out DaoDI>): DaoDI {
            return instance?.daoMap?.get(type.simpleName)
                ?: error("${type.simpleName} 해당 interface에 대한 객체가 없습니다.")
        }

        companion object {
            private var instance: DaoModule? = null

            fun initLifeCycle(context: Context) {
                require(instance == null) {
                    "Module은 한번만 초기화가 가능합니다."
                }

                val newInstance = DaoModule(context)
                when (context) {
                    is Application -> ProcessLifecycleOwner.get().lifecycle.addObserver(newInstance)
                    is AppCompatActivity -> context.lifecycle.addObserver(newInstance)
                    is Fragment -> context.lifecycle.addObserver(newInstance)
                    else -> error("해당 로직은 Application, AppCompatActivity, Fragment에서만 쓸 수 있습니다.")
                }
                instance = newInstance
            }

            fun getInstance(): DaoModule = instance ?: error("Module에 대한 lifecycle이 지정되지 않았습니다.")

            fun getInstanceOrNull(): DaoModule? = instance
        }
    }
