package com.nathan.widget.app

import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivitySeekBarBinding
import com.nathan.widget.ui.view.NatureToast
import com.nathan.widget.util.enable

class SeekBarActivity : BaseActivity() {

    private lateinit var binding: ActivitySeekBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnDisable.setOnClickListener {
            val seekBar = binding.seekBar
            seekBar.enable(seekBar.isEnabled.not())
        }
        binding.increaseProgress.setOnClickListener {
            val seekBar = binding.seekBar
            val progress = seekBar.progress
            val max = seekBar.max
            if (progress < max && max - progress >= 5) {
                seekBar.progress += 5
            } else {
                seekBar.progress = max
            }
        }
        binding.decreaseProgress.setOnClickListener {
            val seekBar = binding.seekBar
            val progress = seekBar.progress
            val min = seekBar.min
            if (seekBar.progress > seekBar.min && progress - min >= 5) {
                seekBar.progress -= 5
            } else {
                seekBar.progress = min
            }
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                NatureToast.makeText(
                    this@SeekBarActivity,
                    "进度$progress，用户行为：$fromUser",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                NatureToast.makeText(this@SeekBarActivity, "开始拖动", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                NatureToast.makeText(this@SeekBarActivity, "停止拖动", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun getViewBinding(): ViewBinding {
        return ActivitySeekBarBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}