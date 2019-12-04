package com.agenkan.customer.presentation.main

import android.os.Bundle
import androidx.lifecycle.Observer
import com.agenkan.customer.R
import com.agenkan.customer.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val model: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model.getUser().observe(this, Observer {
            textViewName.text = it.name
            textViewId.text = it.id
            textViewState.text = getString(R.string.text_active, it.isActive)
        })

    }
}