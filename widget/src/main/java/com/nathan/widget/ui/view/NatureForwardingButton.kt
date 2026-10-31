package com.nathan.widget.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.nathan.widget.R
import com.nathan.widget.util.gone
import com.nathan.widget.util.visibility
import com.nathan.widget.util.visible

class NatureForwardingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureForwardingButtonStyle
) : ConstraintLayout(context, attrs, defAttr), Checkable {

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }

    private lateinit var text: NatureCheckableTextView
    private lateinit var icon: ImageView
    private lateinit var arrow: ImageView

    private var checked = false
    private var checkable = true
    private var isForwarding = true

    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.na_view_forwarding_button, this)
        setupView()
        context.withStyledAttributes(
            attrs,
            R.styleable.NatureForwardingButton,
            defAttr,
            R.style.Widget_Nature_Button_Forwarding
        ) {
            val isForwarding = getBoolean(R.styleable.NatureForwardingButton_isForwarding, true)
            setForwarding(isForwarding)
            val iconDrawable = getDrawable(R.styleable.NatureForwardingButton_android_icon)
            setIcon(iconDrawable)
            val s = getString(R.styleable.NatureForwardingButton_android_text) ?: ""
            setText(s)
            val checkable = getBoolean(R.styleable.NatureListItemView_android_checkable, true)
            setCheckable(checkable)
            val checked = getBoolean(R.styleable.NatureListItemView_android_checked, false)
            isChecked = checked
        }
    }

    fun setForwarding(isForwarding: Boolean) {
        this.isForwarding = isForwarding
        // apply gravity
        if (isForwarding) {
            // 靠左[icon+tv---arrow]
            arrow.visible()
            (icon.layoutParams as LayoutParams).apply {
                endToStart = View.NO_ID
            }
            (text.layoutParams as LayoutParams).apply {
                // 占满剩余
                width = 0
                endToStart = arrow.id
            }
        } else {
            // 居中[---icon+tv---]
            arrow.gone()
            (icon.layoutParams as LayoutParams).apply {
                endToStart = text.id
                // 内收模式
                horizontalChainStyle = LayoutParams.CHAIN_PACKED
            }
            (text.layoutParams as LayoutParams).apply {
                endToEnd = LayoutParams.PARENT_ID
                width = LayoutParams.WRAP_CONTENT
            }
        }
    }

    fun isForwarding() = isForwarding

    fun setText(s: String?) {
        text.text = s
    }

    fun setIcon(iconDrawable: Drawable?) {
        icon.setImageDrawable(iconDrawable)
        icon.visibility(iconDrawable != null)
        // 前进模式才需要，非前进模式，居中显示，无需调整
        if (isForwarding) {
            if (iconDrawable == null) {
                // 移除文案左边margin
                (text.layoutParams as MarginLayoutParams).apply {
                    marginStart = 0
                }
            } else {
                // 还原文案左边margin
                (text.layoutParams as MarginLayoutParams).apply {
                    val typedValue = TypedValue()
                    context.theme.resolveAttribute(R.attr.marginSmall, typedValue, true)
                    marginStart = resources.getDimensionPixelOffset(typedValue.resourceId)
                }
            }
        }
    }

    private fun setupView() {
        text = findViewById(android.R.id.text1)
        icon = findViewById(android.R.id.icon)
        arrow = findViewById(android.R.id.icon1)
        // 同步颜色状态
        val textColors = text.textColors
        if (textColors != null) {
            icon.imageTintList = textColors
            arrow.imageTintList = textColors
        }
    }

    fun setCheckable(checkable: Boolean) {
        this.checkable = checkable
        if (checkable.not()) {
            isChecked = false
        }
    }

    fun isCheckable() = checkable

    override fun isChecked(): Boolean {
        return checked
    }

    /**
     * 同步activated与checked一致
     */
    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        setCheckable(activated)
    }

    override fun setChecked(checked: Boolean) {
        if (isEnabled && isCheckable() && this.checked != checked) {
            this.checked = checked
            // 同步文案
            text.isChecked = checked
            // 同步图标[ImageView本身无checkable状态，借用activated]
            icon.isActivated = checked
            arrow.isActivated = checked
            refreshDrawableState()
            onCheckedChangeListener?.onCheckedChanged(checked)
        }
    }

    override fun toggle() {
        checked = checked.not()
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        onCheckedChangeListener = listener
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (checked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }


    /**
     * 选择监听器, 当添加了相关监听器之后，一旦选择状态发生变化便会通知
     */
    interface OnCheckedChangeListener {
        /**
         * Checked 状态变化时通知
         *
         * @param checked 是否被选中
         */
        fun onCheckedChanged(checked: Boolean)
    }
}