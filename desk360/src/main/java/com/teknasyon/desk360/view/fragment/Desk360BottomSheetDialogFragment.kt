package com.teknasyon.desk360.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360CustomStyle

class Desk360BottomSheetDialogFragment(val listener: BottomSheetListener) :
    BottomSheetDialogFragment() {

    private var bottomSheetListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.desk360_bottom_sheet_layout, container, false)

        val buttonImage = view.findViewById<TextView>(R.id.btnImageAttachment)
        val buttonPdf = view.findViewById<TextView>(R.id.btnPdfAttachment)
        bottomSheetListener = listener

        Desk360CustomStyle.setFontWeight(buttonImage,context,500)
        Desk360CustomStyle.setFontWeight(buttonPdf,context,500)

        buttonImage.setOnClickListener {
            bottomSheetListener?.onButtonClicked(true)
            dismiss()
        }

        buttonPdf.setOnClickListener {
            bottomSheetListener?.onButtonClicked(false)
            dismiss()
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            buttonPdf.visibility=View.GONE
        }

        return view
    }

    interface BottomSheetListener {
        fun onButtonClicked(isClickedImage: Boolean)
    }
}