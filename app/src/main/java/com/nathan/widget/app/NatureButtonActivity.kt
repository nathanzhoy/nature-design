package com.nathan.widget.app

import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityNatureButtonBinding
import com.nathan.widget.ui.view.NatureToast

class NatureButtonActivity : BaseActivity() {

    private lateinit var binding: ActivityNatureButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnSmall.setOnClickListener {
            NatureToast.makeText(this, "点击小按钮", Toast.LENGTH_SHORT).show()
        }
        binding.btnNormal.setOnClickListener {
            NatureToast.makeText(this, "点击普通按钮", Toast.LENGTH_SHORT).show()
        }
        binding.btnBlue.setOnClickListener {
            NatureToast.makeText(this, "点击主题按钮", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityNatureButtonBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}