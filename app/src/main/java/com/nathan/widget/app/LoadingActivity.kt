package com.nathan.widget.app

import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityLoadingBinding
import com.nathan.widget.ui.view.NatureButton
import com.nathan.widget.ui.view.NatureToast

class LoadingActivity : BaseActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnStopResume.setOnClickListener {
            val running = binding.loadingView.isRunning
            if (running) {
                binding.loadingView.stop()
                NatureToast.makeText(this, "停止将重置动画", Toast.LENGTH_SHORT).show()
            } else {
                binding.loadingView.start()
            }
            val button = it as NatureButton
            button.text = if (running) {
                getString(R.string.stop)
            } else {
                getString(R.string.start)
            }
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityLoadingBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }
}