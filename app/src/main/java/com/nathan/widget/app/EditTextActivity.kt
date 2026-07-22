package com.nathan.widget.app

import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityEditTextBinding

class EditTextActivity: BaseActivity() {

    private lateinit var binding: ActivityEditTextBinding

    override fun getViewBinding(): ViewBinding {
        return ActivityEditTextBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}