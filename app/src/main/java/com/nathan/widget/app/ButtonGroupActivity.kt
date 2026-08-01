package com.nathan.widget.app

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.get
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityButtonGroupBinding
import com.nathan.widget.ui.view.NatureButton
import com.nathan.widget.ui.view.NatureButtonToggleGroup
import com.nathan.widget.ui.view.NatureToast
import com.nathan.widget.util.enable

class ButtonGroupActivity : BaseActivity() {

    private lateinit var binding: ActivityButtonGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 切换item后，重建页面checkButton会失效（实际上是被MaterialButton内部savedState:check:false覆盖了）
        binding.buttonGroupSingle.addOnButtonCheckedListener { group, id, checked ->
            val button = group.findViewById<NatureButton>(id)
            NatureToast.makeText(this, "${button.text} 选中：${checked}", Toast.LENGTH_SHORT).show()
        }

        binding.buttonGroupMulti.addOnButtonCheckedListener { group, id, checked ->
            val button = group.findViewById<NatureButton>(id)
            NatureToast.makeText(this, "${button.text} 选中：${checked}", Toast.LENGTH_SHORT).show()
        }
        binding.buttonGroupUnrequired.addOnButtonCheckedListener { group, id, checked ->
            val button = group.findViewById<NatureButton>(id)
            NatureToast.makeText(this, "${button.text} 选中：${checked}", Toast.LENGTH_SHORT).show()
        }
        binding.btnDisable.setOnClickListener {
            for (i in 0 until binding.main.childCount) {
                val child = binding.main.getChildAt(i)
                if (child is NatureButtonToggleGroup) {
                    child.enable(child.isEnabled.not())
                }
            }
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityButtonGroupBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}