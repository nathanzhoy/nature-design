package com.nathan.widget.app

import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityDialogBinding
import com.nathan.widget.dialog.AlertDialog

class DialogActivity : BaseActivity() {

    private lateinit var binding: ActivityDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.dialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("标题")
                .setMessage("在 Theme 里指定 alertDialogStyle 但自定义布局没生效，这个问题很典型。根据你的描述，最可能的原因是混淆了 alertDialogStyle 和 alertDialogTheme 这两个属性")
                .setPositiveButton("确定") { dialog, which ->

                }.setNegativeButton("取消") { dialog, which ->

                }
                .create()
                .show()
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityDialogBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}