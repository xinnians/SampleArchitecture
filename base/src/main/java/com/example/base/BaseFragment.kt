package com.example.base

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(){

    private lateinit var mSharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = AppInjector.obtainViewModel(activity!!.application)
    }

    public fun getSharedViewModel() : SharedViewModel = mSharedViewModel

    public fun getApplication(): Application? = activity?.application
}