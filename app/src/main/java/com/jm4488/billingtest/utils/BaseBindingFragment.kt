package com.jm4488.billingtest.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<T : ViewDataBinding> : androidx.fragment.app.Fragment() {

    private lateinit var mViewDataBinding: T

    abstract fun initData()

    abstract fun getLayoutRes(): Int

    abstract fun initView(viewDataBinding: T)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(mViewDataBinding)
    }

}