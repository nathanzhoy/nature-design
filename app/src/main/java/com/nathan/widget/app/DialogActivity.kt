package com.nathan.widget.app

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityDialogBinding
import com.nathan.widget.dialog.AlertDialog

class DialogActivity : BaseActivity() {

    private lateinit var binding: ActivityDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.dialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.title)
                .setMessage(R.string.test_text_long)
                .setPositiveButton(R.string.confirm) { dialog, which ->

                }.setNegativeButton(R.string.cancel) { dialog, which ->

                }
                .create()
                .show()
        }
        binding.scrollDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.title)
                .setMessage(R.string.test_text_super_long)
                .setPositiveButton(R.string.confirm) { dialog, which ->

                }.setNegativeButton(R.string.cancel) { dialog, which ->

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