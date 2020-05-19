package com.example.page_login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.color
import com.example.base.BaseFragment
import com.example.resource.*
import me.vponomarenko.injectionmanager.x.XInjectionManager
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast

class RegisterFragment : BaseFragment() {
    private val btnRegister by lazy { find<Button>(R.id.btnRegister) }
    private val etAccount by lazy { find<EditText>(R.id.etAccount) }
    private val etPws by lazy { find<EditText>(R.id.etPws) }
    private val tvHide by lazy { find<TextView>(R.id.tvHide) }
    private val tvAccountMsg by lazy { find<TextView>(R.id.tvAccountMsg) }
    private val tvPwsMsg by lazy { find<TextView>(R.id.tvPwsMsg) }
    private val tvLoginMsg by lazy { find<TextView>(R.id.tvLoginMsg) }
    private var isHide = true
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

        btnRegister.text = "免費註冊"
        btnRegister.onClick {
            if (isAccount && isPws) {
                toast("註冊成功")
                prefStore.account = etAccount.text.toString()
                prefStore.password = etPws.text.toString()
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