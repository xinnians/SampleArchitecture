package com.example.samplearchitecture.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation.findNavController
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import com.example.base.navbar_tool.MeowBottomNavigation
import com.example.samplearchitecture.Navigator
import com.example.samplearchitecture.R
import kotlinx.android.synthetic.main.activity_main.*
import me.vponomarenko.injectionmanager.x.XInjectionManager


class MainActivity : BaseActivity() {

    companion object {
        /**
         * NavigationBar ID
         */
        private const val ID_NOTICE = 1
        private const val ID_GIFT = 2
        private const val ID_DEPOSIT = 3
        private const val ID_TRANSATION = 4
        private const val ID_USER = 5
        private const val ID_HOME = 6

        /**
         * subCells ID
         */
        private const val ID_SUB_MESSAGE = 7
        private const val ID_SUB_ANNOUNCEMENT = 8
        private const val ID_SUB_CS = 9
        var isBottomNavHide: Boolean = true
    }

    private val navigator: Navigator by lazy {
        XInjectionManager.findComponent<Navigator>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNavBar()
    }

    override fun hideBottomNav() {
        if (isBottomNavHide) {
            meowNavBar.visibility = View.INVISIBLE
        } else {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            meowNavBar.startAnimation(animation)
            meowNavBar.visibility = View.INVISIBLE
        }
        isBottomNavHide = true
    }

    override fun showBottomNav() {
        if (isBottomNavHide) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            meowNavBar.startAnimation(animation)
            meowNavBar.visibility = View.VISIBLE
        } else {
            meowNavBar.visibility = View.VISIBLE
        }
        isBottomNavHide = false
    }

    override fun onResume() {
        super.onResume()
        navigator.bind(findNavController(this, R.id.layout_fragment))
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(this, R.id.layout_fragment).navigateUp()

    private fun initBottomNavBar() {
//        meowNavBar.visibility = View.GONE
        meowNavBar.let {
            it.add(MeowBottomNavigation.Model(ID_NOTICE,
                                              R.drawable.ic_notice,
                                              arrayListOf(MeowBottomNavigation.Model(ID_SUB_MESSAGE, R.drawable.ic_message),
                                                          MeowBottomNavigation.Model(ID_SUB_ANNOUNCEMENT, R.drawable.ic_announcement),
                                                          MeowBottomNavigation.Model(ID_SUB_CS, R.drawable.ic_cs))))
            it.add(MeowBottomNavigation.Model(ID_GIFT, R.drawable.ic_gift))
            it.add(MeowBottomNavigation.Model(ID_DEPOSIT, R.drawable.ic_deposit))
            it.add(MeowBottomNavigation.Model(ID_TRANSATION, R.drawable.ic_transaction))
            it.add(MeowBottomNavigation.Model(ID_USER, R.drawable.ic_user))
            it.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home))
            // navBar icon 加入 badgeNumber
//            it.setCount(ID_NOTICE, "222")
            it.setCount(ID_NOTICE, "")
            // subCells icon 加入 badgeNumber
            it.setSubItemBadgeDraw(ID_SUB_MESSAGE, "3")
            // 初始設定自動彈出第一個 NavBar icon
            it.show(ID_HOME)
            it.setOnShowListener {
//                Log.d("msg", "id: ${it.id}")
//                Log.d("msg", "onShowListener")
            }
            it.setOnClickMenuListener {
                // BottomNav 項目
                when (it.id) {
                    ID_GIFT -> {
                        navigator.giftPage()
                    }
                    ID_DEPOSIT -> {
                        navigator.depositPage()
                    }
                    ID_TRANSATION -> {
                        navigator.transationPage()
                    }
                    ID_USER -> {
                        navigator.userPage()
                    }
                    ID_HOME -> {
                        navigator.homePage()
                    }
                    else -> {
                    }
                }
            }
            it.setOnReselectListener { }
            it.setCellOnClickListener(object : MeowBottomNavigation.CellOnClickListener {
                override fun cellOnClickListener(id: Int) {
                    // BottomNav 子項目
                    when (id) {
                        ID_SUB_MESSAGE -> {
                        }
                        ID_SUB_ANNOUNCEMENT -> {
                        }
                        ID_SUB_CS -> {
                        }
                        else -> {
                        }
                    }
                }
            })
        }
    }
}