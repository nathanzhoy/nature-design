package com.nathan.widget.ui.list

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.setPadding
import com.nathan.widget.R
import com.nathan.widget.databinding.NaViewListItemViewBinding
import com.nathan.widget.util.PressAnimUtils
import com.nathan.widget.util.ScreenUtils

class NatureListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureListItemViewStyle
) : ConstraintLayout(context, attrs, defAttr) {

    private var binding: NaViewListItemViewBinding = NaViewListItemViewBinding.inflate(LayoutInflater.from(context), this)
    private var isForwarding = false
    private var title = ""
    private var summary = ""

    init {
        PressAnimUtils(this)
        context.withStyledAttributes(attrs, R.styleable.NatureListItemView, defAttr) {
            isForwarding = getBoolean(R.styleable.NatureListItemView_isForwarding, false)
            if (isForwarding) {

            }
            title = getString(R.styleable.NatureListItemView_android_title) ?: ""
            setTitle(title)
            summary = getString(R.styleable.NatureListItemView_android_summary) ?: ""
            setSummary(summary)
        }
    }

    fun setSummary(summary: String) {
        this.summary = summary
        this.binding.summary.text = summary
        // Show summary
        if (this.binding.summary.visibility != VISIBLE) {
            this.binding.summary.visibility = VISIBLE
        }
    }

    fun setTitle(title: String) {
        this.title = title
        this.binding.title.text = title
    }

    fun setTitle(title: CharSequence) {
        setTitle(title.toString())
    }
}