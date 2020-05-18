package com.example.base

import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class ResourceProvider(var application: Application) {

    fun resources(): Resources = application.resources

    fun getString(stringRes: Int): String = application.getString(stringRes)

    fun getColor(@ColorRes colorRes: Int): Int = ContextCompat.getColor(application, colorRes)

    fun getDrawable(@DrawableRes drawableRes: Int): Drawable = ContextCompat.getDrawable(application, drawableRes)!!
}