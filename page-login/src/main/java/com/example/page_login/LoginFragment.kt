package com.example.page_login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.color
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.repository.model.ViewState
import com.example.resource.*
import me.vponomarenko.injectionmanager.x.XInjectionManager
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast

class LoginFragment : BaseFragment() {
    private val btnLogin by lazy { find<Button>(R.id.btnLogin) }
    private val etAccount by lazy { find<EditText>(R.id.etAccount) }
    private val etPws by lazy { find<EditText>(R.id.etPws) }
    private val tvHide by lazy { find<TextView>(R.id.tvHide) }
    private val tvAccountMsg by lazy { find<TextView>(R.id.tvAccountMsg) }
    private val tvPwsMsg by lazy { find<TextView>(R.id.tvPwsMsg) }
    private val tvRegisterMsg by lazy { find<TextView>(R.id.tvRegisterMsg) }
    private val cbRemember by lazy { find<CheckBox>(R.id.cbRemember) }
    private val ivTestPlay by lazy { find<ImageView>(R.id.ivTestPlay) }
    private var isHide = true
    private var isAccount = false
    private var isPws = false

    private lateinit var mViewModel: LoginViewModel

    private lateinit var prefStore: PreferenceStore

    private val navigation: LoginNavigation by lazy {
        XInjectionManager.findComponent<LoginNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_login)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefStore = PreferenceStore(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        init()
        setListener()
    }

    private fun init(){
        mViewModel = AppInjector.obtainViewModel(this)
//        btnUp.setOnClickListener { navigation.openLoginUp() }
//        btnDown.setOnClickListener { navigation.openLoginDown() }

        mViewModel.getLoginResult().observeNotNull(this){ state ->
            when(state) {
                is ViewState.Success -> {
                    Log.e("Ian","ViewState.Success : ${state.data.token}")
                    getSharedViewModel().lotteryToken.postValue(state.data.token)
                }
                is ViewState.Loading -> Log.e("Ian","ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }
    }

    private fun setListener() {
        cbRemember.isChecked = prefStore.remember
        if (cbRemember.isChecked) {
            etAccount.setText(prefStore.account)
            etPws.setText(prefStore.password)
            isAccount = true
            isPws = true
        }

        etAccount.addTextChangedListener(object : TextWatcherSon() {
            override fun textChanged(editable: Editable) {
                if (editable.toString().length < 6) {
                    etAccount.backgroundResource = R.drawable.radius_err_board
                    tvAccountMsg.let {
                        it.visible()
                        it.text = "6-16 字母或数字"
                        isAccount = false
                    }

                } else {
                    etAccount.backgroundResource = R.drawable.radius_board
                    tvAccountMsg.let {
                        it.gone()
                        it.text = ""
                        isAccount = true
                    }
                }
            }
        })

        etPws.addTextChangedListener(object : TextWatcherSon() {
            override fun textChanged(editable: Editable) {
                if (editable.toString().length < 6) {
                    etPws.backgroundResource = R.drawable.radius_err_board
                    tvPwsMsg.let {
                        it.visible()
                        it.text = "6-16 字母或数字"
                        isPws = false
                    }

                } else {
                    etPws.backgroundResource = R.drawable.radius_board
                    tvPwsMsg.let {
                        it.gone()
                        it.text = ""
                        isPws = true
                    }
                }
            }
        })

        cbRemember.onClick {
            prefStore.remember = cbRemember.isChecked
        }

        tvHide.onClick {
            if (isHide) {
                isHide = false
                tvHide.text = "hide"
                etPws.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                isHide = true
                tvHide.text = "show"
                etPws.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        ivTestPlay.onClick {
            toast("試玩版開發中")
        }

        btnLogin.text = "登入"
        btnLogin.onClick {
            if (isAccount && isPws) {
                if ((etAccount.text.toString() == prefStore.account && etPws.text.toString() == prefStore.password)) {
                    toast("登入成功")
                } else {
                    toast("帳號密碼錯誤")
                }
            } else {
                toast("請輸入帳號密碼喔")
            }
        }

        val s = SpannableStringBuilder()
            .append("尚未註冊？")
            .color(Color.RED) { append("免費註冊") }

        tvRegisterMsg.text = s
        tvRegisterMsg.onClick {
            navigation.registerPage()
        }
    }
}