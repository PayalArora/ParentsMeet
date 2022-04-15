package com.logicsquare.parentsmeet.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.utils.gone


class VInputDisplay : ConstraintLayout {
    private lateinit var tvText: VTextView
    private lateinit var ivPrimary: ImageView
    private lateinit var ivSecondaryIcon: ImageView

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
        inflate(context, R.layout.input_display_layout, this)
        tvText = findViewById(R.id.tvLabel)
        ivPrimary = findViewById(R.id.ivPrimaryIcon)
        ivSecondaryIcon = findViewById(R.id.ivSecondaryIcon)
        ivSecondaryIcon.gone()
        val ta = context.obtainStyledAttributes(attrs, R.styleable.VInputDisplay)
        val text = ta.getString(R.styleable.VInputDisplay_android_text)
        tvText.text = text
        val hint = ta.getString(R.styleable.VInputDisplay_android_hint)
        tvText.hint = hint
        ta.getResourceId(R.styleable.VInputDisplay_android_src, -1).let {
            if (it > 0) {
                ivPrimary.visibility = View.VISIBLE
                ivPrimary.setImageResource(it)
            } else {
                ivPrimary.visibility = View.GONE
                val params: LayoutParams = tvText.layoutParams as LayoutParams
                params.setMargins(0,0,0,0)
                tvText.layoutParams = params
            }
        }
        ta.recycle()
    }

    fun setText(text: String?){
        text?.let { tvText.text = text }
    }

}