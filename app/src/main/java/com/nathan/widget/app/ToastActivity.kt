package com.nathan.widget.app

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityToastBinding
import com.nathan.widget.ui.view.NatureToast

class ToastActivity : BaseActivity() {

    private lateinit var binding: ActivityToastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toastText.setOnClickListener {
            NatureToast.makeText(this, "普通吐司提示", Toast.LENGTH_SHORT).show()
        }

        binding.toastIcon.setOnClickListener {
            NatureToast.makeText(
                this,
                "带图吐司提示",
                Toast.LENGTH_SHORT,
                ContextCompat.getDrawable(this, com.nathan.widget.R.drawable.na_ic_close)
            ).show()
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityToastBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}