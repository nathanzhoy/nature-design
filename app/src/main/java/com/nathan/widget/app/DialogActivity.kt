package com.nathan.widget.app

import android.R.attr.text
import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityDialogBinding
import com.nathan.widget.dialog.AlertDialog
import com.nathan.widget.ui.view.NatureToast

class DialogActivity : BaseActivity() {

    companion object {
        private val ITEMS =
            arrayOf("淘宝", "支付宝", "京东", "拼夕夕", "抖音", "哔哩哔哩", "小红书")
    }

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
                .setNeutralButton(R.string.neutral) { dialog, which ->

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
        binding.singleListDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.title)
                // 设置关闭图点击监听，设置后图标才会显示
                .setOnCloseIconClickListener { _, which ->
                    NatureToast.makeText(this, "点击关闭图标", Toast.LENGTH_SHORT).show()
                }
                .setSingleChoiceItems(ITEMS, 0) { _, which ->
                    NatureToast.makeText(this, "${ITEMS[which]}被选中", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton(R.string.confirm) { dialog, which ->

                }.setNegativeButton(R.string.cancel) { dialog, which ->

                }
                .create()
                .show()
        }
        binding.multiListDialog.setOnClickListener {
            val selected = mutableListOf<String>()
            AlertDialog.Builder(this)
                .setTitle(R.string.title)
                .setMultiChoiceItems(ITEMS, null) { _, which, isChecked ->
                    val text = ITEMS[which]
                    NatureToast.makeText(
                        this,
                        "${text}被选中：$isChecked",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (isChecked) {
                        selected.add(text)
                    }
                }
                .setPositiveButton(R.string.confirm) { dialog, which ->
                    NatureToast.makeText(
                        this,
                        "你选中了：$selected",
                        Toast.LENGTH_SHORT
                    ).show()
                }.setNegativeButton(R.string.cancel) { dialog, which ->

                }
                .create()
                .show()
        }

        binding.singleButtonDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.title)
                .setMessage(R.string.test_text_long)
                .setCountDown(1000 * 10, true)
                .setPositiveButton(R.string.confirm) { dialog, which ->

                }
                .create()
                .show()
        }

        binding.dangerDialog.setOnClickListener {
            AlertDialog.Builder(this, com.nathan.widget.R.style.Theme_Nature_Dialog_Danger)
                .setTitle(R.string.title)
                .setMessage(R.string.test_text_long)
                .setCountDown(1000L * 5, false)
                .setPositiveButton(R.string.confirm) { dialog, which ->

                }
                .setNegativeButton(R.string.cancel) { dialog, which ->

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