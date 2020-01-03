package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class TextViewBold : TextView {

    constructor(context: Context) : super(context) {
        val face = Typeface.createFromAsset(context.assets, "ProximaNova")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val face = Typeface.createFromAsset(context.assets, "ProximaNova-Bold.otf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val face = Typeface.createFromAsset(context.assets, "ProximaNova-Bold.otf")
        this.typeface = face
    }
}