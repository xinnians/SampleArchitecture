package com.example.base

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseFragment : Fragment(), CoroutineScope {

    private lateinit var mSharedViewModel: SharedViewModel

    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        mSharedViewModel = AppInjector.obtainViewModel(activity!!.application)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    public fun getSharedViewModel(): SharedViewModel = mSharedViewModel

    public fun getApplication(): Application? = activity?.application
}