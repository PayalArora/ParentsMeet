package com.logicsquare.parentsmeet.views

import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import com.logicsquare.parentsmeet.R


class VInputField : ConstraintLayout {
    private lateinit var etInput: VEditText
    private lateinit var ivPrimary: ImageView
    private lateinit var ivSecondary: ImageView
    private var passwordMode: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        inflate(context, R.layout.input_field_layout, this)
        etInput = findViewById(R.id.etInput)
        ivPrimary = findViewById(R.id.ivPrimaryIcon)
        ivSecondary = findViewById(R.id.ivSecondaryIcon)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.VInputField)
        val hint = ta.getString(R.styleable.VInputField_label)
        etInput.hint = hint
        ta.getResourceId(R.styleable.VInputField_android_src, -1).let {
            if (it > 0) {
                ivPrimary.visibility = View.VISIBLE
                ivPrimary.setImageResource(it)
            } else {
                ivPrimary.visibility = View.GONE
                val params: LayoutParams = etInput.layoutParams as LayoutParams
                params.setMargins(0, 0, 0, 0)
                etInput.layoutParams = params
            }
        }
        val inputType =
            ta.getInt(R.styleable.VInputField_android_inputType, InputType.TYPE_CLASS_TEXT)
        etInput.inputType = inputType
        passwordMode = ta.getBoolean(R.styleable.VInputField_password, false)
        val id = ta.getResourceId(R.styleable.VInputField_secondaryIcon, -1)
        if (id > 0) {
            ivSecondary.visibility = View.VISIBLE
            ivSecondary.setImageResource(id)
            ivSecondary.setOnClickListener {
                togglePasswordVisibility()
            }
        } else {
            ivSecondary.visibility = View.GONE
        }
        var fontName = ta.getString(R.styleable.VInputField_myFont)
        if (fontName.isNullOrBlank()) {
            fontName = resources.getString(R.string.font_regular)
        }
        val typeface = Typeface.createFromAsset(context.assets, fontName)
        etInput.typeface = typeface
        val maxLength = ta.getInt(R.styleable.VInputField_android_maxLength, 99)
        etInput.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
        val text = ta.getString(R.styleable.VInputField_android_text)
        text?.let { etInput.setText(text.toString()) }
        val enabled = ta.getBoolean(R.styleable.VInputField_android_enabled, true)
        etInput.isEnabled = enabled
        val singleLine = ta.getBoolean(R.styleable.VInputField_android_singleLine, true)
        etInput.isSingleLine = singleLine
        ta.recycle()
    }

    private fun togglePasswordVisibility() {
        if (passwordMode) {
            passwordMode = false
            etInput.inputType = InputType.TYPE_CLASS_TEXT
        } else {
            passwordMode = true
            etInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        etInput.setSelection(etInput.text?.length ?: 0)
    }

    fun setPasswordMode(){
        passwordMode = true
        etInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    fun getText(): String {
        return etInput.text.toString().trim()
    }

    fun setText(text: String?) {
        text?.let { etInput.setText(it) }
    }
}