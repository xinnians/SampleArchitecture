package com.example.resource

import android.text.Editable
import android.text.TextWatcher

abstract class TextWatcherSon : TextWatcher {

    override fun afterTextChanged(editable: Editable) { textChanged(editable) }

    override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)  {}

    abstract fun textChanged(editable: Editable)
}