package com.nathan.widget.app

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityBaseBinding

abstract class BaseActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(getViewBinding().root)
        setupToolbar()
    }

    private fun setupToolbar() {
        super.setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(this@BaseActivity !is MainActivity)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        throw IllegalAccessException("Toolbar has been used for base activity")
    }


    override fun setContentView(view: View?) {
        // 套上统一带Toolbar的布局
        binding.content.addView(view)
        super.setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun getViewBinding(): ViewBinding
}