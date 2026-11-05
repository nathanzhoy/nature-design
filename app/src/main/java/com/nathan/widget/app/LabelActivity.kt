package com.nathan.widget.app

import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityLabelBinding
import com.nathan.widget.ui.view.NatureButton
import com.nathan.widget.ui.view.NatureToast
import com.nathan.widget.util.enable

class LabelActivity : BaseActivity() {

    private lateinit var binding: ActivityLabelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnNormal.setOnClickListener {
            NatureToast.makeText(this, "点击普通按钮", Toast.LENGTH_SHORT).show()
        }
        binding.btnNormal.setOnLongClickListener {
            val button = it as NatureButton
            val checked = button.isChecked
            button.isChecked = !checked
            NatureToast.makeText(this, "普通按钮选中：${button.isChecked}", Toast.LENGTH_SHORT)
                .show()
            true
        }
        binding.btnBlue.setOnClickListener {
            NatureToast.makeText(this, "点击主题按钮", Toast.LENGTH_SHORT).show()
        }

        binding.btnDisable.setOnClickListener {
            val count = binding.main.childCount
            for (i in 0 until count) {
                val child = binding.main.getChildAt(i)
                if (child is NatureButton && child.id != R.id.btn_disable) {
                    child.enable(child.isEnabled.not())
                }
            }
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityLabelBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}