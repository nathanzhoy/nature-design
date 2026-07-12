package com.nathan.widget.app

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivitySmoothLayoutBinding

class SmoothLayoutActivity: BaseActivity() {

    private lateinit var binding: ActivitySmoothLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(): ViewBinding {
        return ActivitySmoothLayoutBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}