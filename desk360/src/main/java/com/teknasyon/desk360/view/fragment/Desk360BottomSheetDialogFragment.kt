package com.teknasyon.desk360.view.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants
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

        val layout = view.findViewById<LinearLayout>(R.id.sheetRootLayout)
        val buttonImage = view.findViewById<TextView>(R.id.btnImageAttachment)
        val buttonPdf = view.findViewById<TextView>(R.id.btnPdfAttachment)
        val buttonVideo = view.findViewById<TextView>(R.id.btnvideoAttachment)
        buttonImage.text =
            Desk360Constants.currentType?.data?.general_settings?.attachment_images_text
                ?: "Images"
        buttonPdf.text =
            Desk360Constants.currentType?.data?.general_settings?.attachment_browse_text
                ?: "Document"
        buttonVideo.text =
            Desk360Constants.currentType?.data?.general_settings?.attachment_videos_text
                ?: "Videos"

        bottomSheetListener = listener

        layout.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.main_background_color))
        buttonImage.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
        buttonPdf.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
        buttonVideo.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))

        Desk360CustomStyle.setFontWeight(buttonImage, context, 500)
        Desk360CustomStyle.setFontWeight(buttonPdf, context, 500)
        Desk360CustomStyle.setFontWeight(buttonVideo, context, 500)

        buttonImage.setOnClickListener {
            bottomSheetListener?.onButtonClicked(0)
            dismiss()
        }
        buttonVideo.setOnClickListener {
            bottomSheetListener?.onButtonClicked(1)
            dismiss()
        }

        buttonPdf.setOnClickListener {
            bottomSheetListener?.onButtonClicked(2)
            dismiss()
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            buttonPdf.visibility = View.GONE
        }

        return view
    }

    interface BottomSheetListener {
        fun onButtonClicked(typeOfAttachment: Int)
    }
}