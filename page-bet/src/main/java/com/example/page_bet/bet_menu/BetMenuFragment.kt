package com.example.page_bet.bet_menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.*
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.BetFragment.Companion.TAG_GAME_ID
import com.example.page_bet.bet.BetFragment.Companion.TAG_GAME_NAME
import com.example.page_bet.bet.BetFragment.Companion.TAG_GAME_TYPE
import com.example.repository.model.bet.GameMenuResponse
import com.example.repository.model.base.ViewState
import kotlinx.android.synthetic.main.fragment_bet_menu.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

/**
 * 開啟該頁後會跟後台要遊戲選單列表(BetMenuViewModel.getGameMenuResult)，但目前沒有當期開獎時間欄位所以時間暫時顯示00:00:00
 * 要到遊戲選單列表後，會存一份在shareViewModel(基於application的viewModel)，提供其他頁面使用(ex:開獎中心頁面就會需要用到)
 * 點擊任一項目會帶著該項目的gameId,gameName,gameTypeId進BetFragment
 */
class BetMenuFragment : BaseFragment() {

    private lateinit var mBetMenuViewModel: BetMenuViewModel

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_bet_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BaseActivity?)?.hideBottomNav()
        init()
    }

    private fun init() {
        mBetMenuViewModel = AppInjector.obtainViewModel(this)

        mBetMenuViewModel.getGameMenuResult(getSharedViewModel().lotteryToken.value.orEmpty())
            .observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> {
                        Log.e("Ian", "ViewState.Success : ${state.data}")
                        getSharedViewModel().gameMenuList.postValue(state.data)

                        var layoutManager = LinearLayoutManager(context)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        rvGameList.layoutManager = layoutManager

                        var adapter: BetMenuAdapter = BetMenuAdapter(state.data)
                        rvGameList.adapter = adapter

                        adapter.setOnItemChildClickListener { adapter, view, position ->
                            when (view.id) {
                                R.id.ivEdit -> {
                                    navigation.toGameFavoritePage()
                                }
                                R.id.tvLotteryCenter -> {
                                    navigation.toLotteryCenterPage()
                                }
                                else -> {

                                }
                            }
                        }
                        adapter.setOnGameClickListener(object : OnGameClick {
                            override fun onClick(msg: GameMenuResponse.Data.GameInfoEntity) {
                                Log.e("Ian", "[OnGameClick] click. msg:$msg")
                                var bundle = Bundle()
                                bundle.putInt(TAG_GAME_ID, msg.gameId)
                                bundle.putString(TAG_GAME_NAME, msg.gameName)
                                bundle.putInt(TAG_GAME_TYPE, msg.gameTypeId)
                                navigation.toBetPage(bundle)
                            }
                        })
                    }
                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
                }
            }

       toolbar.backListener(View.OnClickListener { navigation.backPrePage() })

    }
}