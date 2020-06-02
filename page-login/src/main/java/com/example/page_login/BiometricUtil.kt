package com.example.page_login

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager

object BiometricUtil {

    fun isHardwareAvailable(context: Context): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val bm = BiometricManager.from(context)
            val canAuthenticate = bm.canAuthenticate()
            !(canAuthenticate == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ||
                    canAuthenticate == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE)

        } else {
            false
        }
    }

    fun hasBiometricEnrolled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val bm = BiometricManager.from(context)
            val canAuthenticate = bm.canAuthenticate()
            (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS)
        } else {
            false
        }
    }

}