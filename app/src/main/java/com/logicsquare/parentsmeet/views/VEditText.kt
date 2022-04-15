package com.logicsquare.parentsmeet.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.logicsquare.parentsmeet.R


class VEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val fontName = resources.getString(R.string.font_regular)
        val typeface = Typeface.createFromAsset(context.assets, fontName)
        setTypeface(typeface)
    }
}