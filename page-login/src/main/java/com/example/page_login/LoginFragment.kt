package com.example.page_login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.text.color
import com.example.base.*
import com.example.base.widget.CustomEditTextView
import com.example.repository.model.base.ViewState
import kotlinx.android.synthetic.main.fragment_login.*
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.util.concurrent.Executor

class LoginFragment : BaseFragment() {
    private var isAccount = false
    private var isPws = false
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
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
        if (BiometricUtil.isHardwareAvailable(requireActivity())) {
            Log.d("Mori", "支援生物辨識")
        } else {
            Log.e("Mori", "不不不支援生物辨識")
        }

        if (BiometricUtil.hasBiometricEnrolled(requireActivity())) {
            Log.d("Mori", "已註冊生物識別")
        } else {
            Log.e("Mori", "還沒有註冊生物識別")
        }
        (activity as BaseActivity?)?.hideBottomNav()
    }

    override fun onResume() {
        super.onResume()
        init()
        setListener()
    }

    private fun init(){
        mViewModel = AppInjector.obtainViewModel(this)
        mViewModel.getLoginResult().observeNotNull(this){ state ->
            when(state) {
                is ViewState.Success -> {
                    Log.e("Ian","ViewState.Success : ${state.data.token}")
                    getSharedViewModel().lotteryToken.postValue("Bearer ${state.data.token}")
                }
                is ViewState.Loading -> Log.e("Ian","ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }
    }

    private fun setListener() {
        cbRemember.isChecked = prefStore.remember
        if (cbRemember.isChecked) {
            cetAccount.text = prefStore.account
            cetPws.text = prefStore.password
            isAccount = true
            isPws = true
            changeBackground(isAccount, isPws)
        }

        cetAccount.let {
            it.title = "用戶名"
            it.hint = "請輸入用戶名"
            it.ivEyeVisible = false
            it.text = ""
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
                    changeBackground(isAccount, isPws)
                }
            })
        }


        cetPws.let {
            it.title = "密碼"
            it.hint = "請輸入密碼"
            it.text = ""
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
                    changeBackground(isAccount, isPws)
                }
            })
        }

        cbRemember.onClick {
            prefStore.remember = cbRemember.isChecked
        }

        clTestPlay.onClick {
            Toast.makeText(requireContext(), "試玩版開發中", Toast.LENGTH_SHORT).show()
        }

        tvForget.onClick {
            Toast.makeText(requireContext(), "忘記密碼開發中", Toast.LENGTH_SHORT).show()
        }

        btnLogin.text = "登入"
        btnLogin.onClick {
            if (isAccount && isPws) {
//                if ((cetAccount.text == prefStore.account && cetPws.text == prefStore.password)) {
                    Toast.makeText(requireContext(), "登入成功", Toast.LENGTH_SHORT).show()
                    navigation.mainPage()
//                } else {
//                    Toast.makeText(requireContext(), "帳號密碼錯誤", Toast.LENGTH_SHORT).show()
//                }
            } else {
                Toast.makeText(requireContext(), "請輸入帳號密碼喔", Toast.LENGTH_SHORT).show()
            }
        }

        val s = SpannableStringBuilder()
            .append("尚未註冊？")
            .color(Color.parseColor("#999999")) { append("免費註冊") }

        tvRegisterMsg.text = s
        tvRegisterMsg.onClick {
            navigation.registerPage()
        }

        clBioLogin.onClick {
            executor = ContextCompat.getMainExecutor(requireActivity())
            biometricPrompt = BiometricPrompt(requireActivity(), executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(requireContext(), "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(requireContext(), "登入成功", Toast.LENGTH_SHORT).show()
                        navigation.mainPage()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("登入APP")
                .setSubtitle("透過生物辨識登入")
                .setNegativeButtonText("使用帳號密碼登入")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun changeBackground(account: Boolean, password: Boolean) {
        if (account && password) {
            btnLogin.let {
            it.setBackgroundResource(R.drawable.bg_radius_blue_board)
             it.setTextColor(Color.parseColor("#ffffff"))
            }
        } else {
            btnLogin.let {
                it.setBackgroundResource(R.drawable.bg_radius_gary_board)
                it.setTextColor(Color.parseColor("#b9b9b9"))
            }
        }
    }


}