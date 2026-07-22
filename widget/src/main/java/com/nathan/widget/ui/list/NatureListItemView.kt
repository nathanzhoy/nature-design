package com.nathan.widget.ui.list

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.nathan.widget.R
import com.nathan.widget.util.PressAnimUtils
import com.nathan.widget.util.ScreenUtils
import com.nathan.widget.util.gone
import com.nathan.widget.util.visibility
import com.nathan.widget.util.visible

class NatureListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureListItemViewStyle
) : ConstraintLayout(context, attrs, defAttr) {

    private val inflater = LayoutInflater.from(context)

    private lateinit var avatar: ImageView
    private lateinit var titleView: TextView
    private lateinit var summaryView: TextView
    private lateinit var extraSummaryView: TextView
    private lateinit var tipView: ImageView
    private lateinit var forwardingView: ImageView
    private lateinit var customLayout: FrameLayout

    private var mode = Mode.CUSTOM
    private var title = ""
    private var summary = ""
    private var extraSummary = ""
    private var customLayoutRes = 0

    init {
        PressAnimUtils(this)
        inflater.inflate(R.layout.na_view_list_item_view, this)
        setupView()
        context.withStyledAttributes(
            attrs,
            R.styleable.NatureListItemView,
            defAttr,
            R.style.Widget_Nature_ListItemView
        ) {
            mode = Mode.entries[getInt(R.styleable.NatureListItemView_mode, 0)]
            customLayoutRes = getResourceId(R.styleable.NatureListItemView_android_layout, 0)
            setupInitMode()
            val avatarRes = getDrawable(R.styleable.NatureListItemView_avatar)
            setAvatar(avatarRes)
            title = getString(R.styleable.NatureListItemView_android_title) ?: ""
            setTitle(title)
            summary = getString(R.styleable.NatureListItemView_android_summary) ?: ""
            setSummary(summary)
        }
    }

    fun setOnCustomLayoutClick(listener: OnClickListener) {
        customLayout.setOnClickListener(listener)
    }

    fun setToggleChangeListener(listener: CompoundButton.OnCheckedChangeListener) {
        val toggle = customLayout.findViewById<SwitchCompat>(R.id.toggle)
        if (toggle != null) {
            toggle.setOnCheckedChangeListener(listener)
        }
    }

    fun setDot(show: Boolean) {
        val dot = customLayout.findViewById<TextView>(R.id.dot)
        if (dot != null) {
            dot.visibility(show)
        }
    }

    fun setNumber(num: String, hideZero: Boolean = true) {
        val number = customLayout.findViewById<TextView>(R.id.number)
        if (hideZero && num.toInt() == 0) {
            number.gone()
            return
        }
        if (number != null) {
            number.visibility(num.isNotEmpty())
            number.text = num
        }
    }

    fun getCustomLayout() = customLayout

    private fun setupInitMode() {
        when (mode) {
            Mode.TOGGLE -> {
                forwardingView.gone()
                inflater.inflate(R.layout.na_list_item_toggle, customLayout)
            }

            Mode.DOT -> {
                // 存在背景，先隐藏
                inflater.inflate(R.layout.na_list_item_dot, customLayout).apply {
                    findViewById<TextView>(R.id.dot)?.gone()
                }
            }

            Mode.NUMBER -> {
                // 存在背景，先隐藏
                inflater.inflate(R.layout.na_list_item_number, customLayout).apply {
                    findViewById<TextView>(R.id.number)?.gone()
                }
            }

            Mode.CUSTOM -> {
                if (customLayoutRes != 0) {
                    // 渲染自定义布局
                    inflater.inflate(customLayoutRes, customLayout)
                }
            }
        }
    }

    private fun setupView() {
        avatar = findViewById(R.id.avatar)
        titleView = findViewById(android.R.id.title)
        summaryView = findViewById(android.R.id.summary)
        extraSummaryView = findViewById(R.id.extra_summary)
        tipView = findViewById(R.id.tip)
        forwardingView = findViewById(R.id.forwarding)
        customLayout = findViewById(R.id.custom_layout)
    }

    fun setOnTipViewClickListener(listener: OnClickListener) {
        tipView.setOnClickListener(listener)
    }

    fun setAvatar(@DrawableRes resId: Int) {
        setAvatar(ResourcesCompat.getDrawable(context.resources, resId, context.theme))
    }

    fun setAvatar(drawable: Drawable?) {
        avatar.setImageDrawable(drawable)
        avatar.visibility(drawable != null)
        // 增加标题的marginStart
        titleView.apply {
            val params = layoutParams as MarginLayoutParams
            layoutParams = params.apply {
                marginStart = ScreenUtils.dp2Px(context, if (drawable == null) 0F else 10F).toInt()
            }
        }
    }

    fun setSummary(text: String) {
        summary = text
        summaryView.text = text
        summaryView.visible()
    }

    fun setTitle(text: String) {
        title = text
        titleView.text = title
    }

    fun setExtraSummary(text: String) {
        extraSummary = text
        extraSummaryView.text = text
        extraSummaryView.visibility(text.isNotEmpty())
    }

    fun setTitle(title: CharSequence) {
        setTitle(title.toString())
    }

    enum class Mode {
        CUSTOM,
        TOGGLE,
        DOT,
        NUMBER
    }
}