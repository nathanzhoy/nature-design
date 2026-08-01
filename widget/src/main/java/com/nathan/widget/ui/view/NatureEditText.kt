package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.widget.addTextChangedListener
import com.nathan.widget.R
import com.nathan.widget.util.PressAnimUtils
import com.nathan.widget.util.enable
import com.nathan.widget.util.visibility

class NatureEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureEditTextStyle
) : ConstraintLayout(context, attrs, defAttr) {

    private lateinit var clearView: ImageView
    private lateinit var editText: EditText

    init {
        LayoutInflater.from(context).inflate(R.layout.na_view_edit_text, this)
        setupView()
        context.withStyledAttributes(
            attrs,
            R.styleable.NatureEditText,
            defAttr,
            R.style.Widget_Nature_EditText
        ) {
            val hint = getString(R.styleable.NatureEditText_android_hint) ?: ""
            setHint(hint)
            val text = getString(R.styleable.NatureEditText_android_text) ?: ""
            setText(text)
            val maxLines = getInt(R.styleable.NatureEditText_android_maxLines, 1)
            setMaxLines(maxLines)
            val imeOptions = getInt(R.styleable.NatureEditText_android_imeOptions, 0)
            setImeOptions(imeOptions)
            val inputType = getInt(R.styleable.NatureEditText_android_inputType, 1)
            setInputType(inputType)
            val actionId = getInt(R.styleable.NatureEditText_android_imeActionId, 0)
            val actionLabel = getString(R.styleable.NatureEditText_android_imeActionLabel) ?: ""
            setActionLabel(actionLabel, actionId)
            val showClear = getBoolean(R.styleable.NatureEditText_showClear, false)
            setShowClear(showClear)
        }
    }

    private fun setHint(hint: String) {
        editText.hint = hint
    }

    private fun setText(text: String) {
        editText.setText(text)
    }

    private fun setActionLabel(actionLabel: String, actionId: Int) {
        editText.setImeActionLabel(actionLabel, actionId)
    }

    private fun setInputType(inputType: Int) {
        editText.inputType = inputType
    }

    private fun setImeOptions(imeOptions: Int) {
        editText.imeOptions = imeOptions
    }

    private fun setupView() {
        editText = findViewById(R.id.edit_text)
        clearView = findViewById(R.id.clear)
        clearView.setOnClickListener {
            editText.setText("")
        }
        PressAnimUtils(clearView)
        // Text watcher
        editText.addTextChangedListener {
            if (it == null) return@addTextChangedListener
            clearView.enable(it.isNotEmpty())
        }
    }

    fun setMaxLines(max: Int) {
        editText.maxLines = max
    }

    fun setShowClear(show: Boolean) {
        clearView.visibility(show)
    }

    fun getEditText() = editText

    fun getClearView() = clearView
}