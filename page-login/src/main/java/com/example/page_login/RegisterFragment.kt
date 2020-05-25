package com.example.page_login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import com.example.base.*
import com.example.base.widget.CustomEditTextView
import kotlinx.android.synthetic.main.fragment_register.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class RegisterFragment : BaseFragment() {
    private var isAccount = false
    private var isPws = false
    private lateinit var prefStore: PreferenceStore

    private val navigation: LoginNavigation by lazy {
        XInjectionManager.findComponent<LoginNavigation>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_register)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefStore = PreferenceStore(requireActivity())
        setListener()
    }

    private fun setListener() {
        cetAccount.let {
            it.title = "用戶名"
            it.hint = "請輸入用戶名"
            it.textVisible = false
            it.textChangedListener(object : TextWatcherSon() {
                override fun textChanged(editable: Editable) {
                    if (editable.toString().length < 6) {
                        it.let {
                            it.setBackground(R.drawable.radius_err_board)
                            it.notice = "6-16 字母或数字"
                        }
                        isAccount = false
                    } else {
                        it.let {
                            it.setBackground(R.drawable.radius_board)
                            it.notice = ""
                        }
                        isAccount = true
                    }
                }
            })
        }


        cetPws.let {
            it.title = "密碼"
            it.hint = "請輸入密碼"
            it.inputType = CustomEditTextView.InType.PASSWORD
            it.textChangedListener(object : TextWatcherSon() {
                override fun textChanged(editable: Editable) {
                    if (editable.toString().length < 6) {
                        it.let {
                            it.setBackground(R.drawable.radius_err_board)
                            it.notice = "6-16 字母或数字"
                        }
                        isPws = false

                    } else {
                        it.let {
                            it.setBackground(R.drawable.radius_board)
                            it.notice = ""
                        }
                        isPws = true
                    }
                }
            })
        }

        btnRegister.text = "免費註冊"
        btnRegister.onClick {
            if (isAccount && isPws) {
                toast("註冊成功")
                prefStore.account = cetAccount.text
                prefStore.password = cetPws.text
                navigation.loginPage()
            } else {
                toast("請輸入帳號密碼喔")
            }
        }

        val s = SpannableStringBuilder()
            .append("已有帳號？")
            .color(Color.RED) { append("登入") }

        tvLoginMsg.text = s
        tvLoginMsg.onClick {
            navigation.loginPage()
        }
    }
}