package com.nathan.widget.app

import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityButtonBinding
import com.nathan.widget.app.databinding.ActivityForwardingButtonBinding
import com.nathan.widget.ui.view.NatureButton
import com.nathan.widget.ui.view.NatureForwardingButton
import com.nathan.widget.ui.view.NatureToast
import com.nathan.widget.util.enable

class ForwardingButtonActivity : BaseActivity() {

    private lateinit var binding: ActivityForwardingButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnForwardingIcon.setOnClickListener {
            NatureToast.makeText(this, "点击前进按钮", Toast.LENGTH_SHORT).show()
        }
        binding.btnForwardingIcon.setOnLongClickListener {
            val button = it as NatureForwardingButton
            val checked = button.isChecked
            button.isChecked = !checked
            NatureToast.makeText(this, "前进按钮选中：${button.isChecked}", Toast.LENGTH_SHORT)
                .show()
            true
        }

        binding.btnForwarding.setOnClickListener {
            val button = it as NatureForwardingButton
            val checked = button.isChecked
            button.isChecked = !checked
            NatureToast.makeText(this, "前进按钮选中：${button.isChecked}", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnNotForwardingIcon.setOnClickListener {
            val button = it as NatureForwardingButton
            val checked = button.isChecked
            button.isChecked = !checked
            NatureToast.makeText(this, "非前进按钮选中：${button.isChecked}", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnNotForwarding.setOnClickListener {
            val button = it as NatureForwardingButton
            val checked = button.isChecked
            button.isChecked = !checked
            NatureToast.makeText(this, "非前进按钮选中：${button.isChecked}", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnDisable.setOnClickListener {
            val count = binding.main.childCount
            for (i in 0 until count) {
                val child = binding.main.getChildAt(i)
                if (child is NatureForwardingButton && child.id != R.id.btn_disable) {
                    child.enable(child.isEnabled.not())
                }
            }
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityForwardingButtonBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}