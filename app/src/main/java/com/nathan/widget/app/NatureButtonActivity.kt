package com.nathan.widget.app

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityNatureButtonBinding

class NatureButtonActivity : BaseActivity() {

    private lateinit var binding: ActivityNatureButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityNatureButtonBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}