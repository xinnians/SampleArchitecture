package com.example.base

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.base.widget.LoadingDialog
import com.example.repository.room.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseFragment : Fragment(), CoroutineScope {

    //TODO 實作共用的Loading
    //TODO 實作共用的error handle(token過期等等對應error code的共用處理)
    private lateinit var mSharedViewModel: SharedViewModel
    lateinit var job: Job
    private var mLoadingDialog: LoadingDialog? = null

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

    fun getSharedViewModel(): SharedViewModel = mSharedViewModel

    fun getApplication(): Application? = activity?.application

    val showDefaultLoading = { isShow: Boolean ->
        if(mLoadingDialog == null){
            mLoadingDialog = context?.let { LoadingDialog(it) }
        }
        if(isShow){
            mLoadingDialog?.show()
        }else{
            if(mLoadingDialog?.isShowing == true){
                mLoadingDialog?.dismiss()
            }else{

            }
        }
    }
}