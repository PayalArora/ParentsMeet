package com.logicsquare.parentsmeet.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.logicsquare.parentsmeet.R

class VButton : AppCompatButton {
    constructor(context: Context) : super(context) {}
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

    private fun init(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
        var fontName = ta.getString(R.styleable.CustomTextView_myFont)
        if (fontName.isNullOrBlank()) {
            fontName = resources.getString(R.string.font_regular)
        }
        val typeface = Typeface.createFromAsset(context.assets, fontName)
        setTypeface(typeface)
        ta.recycle()
    }
}