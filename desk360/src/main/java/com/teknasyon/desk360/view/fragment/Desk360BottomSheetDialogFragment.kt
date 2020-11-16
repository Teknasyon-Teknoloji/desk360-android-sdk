package com.teknasyon.desk360.view.fragment

import android.graphics.Color
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

class Desk360BottomSheetDialogFragment(
    private val listener: BottomSheetListener
) : BottomSheetDialogFragment() {

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

        val generalSettings = Desk360Constants.currentType?.data?.general_settings

        buttonImage.text = generalSettings?.attachment_images_text ?: "Images"
        buttonPdf.text = generalSettings?.attachment_browse_text ?: "Document"
        buttonVideo.text = generalSettings?.attachment_videos_text ?: "Videos"

        layout.setBackgroundColor(Color.parseColor(generalSettings?.main_background_color))
        buttonImage.setTextColor(Color.parseColor(generalSettings?.header_text_color))
        buttonPdf.setTextColor(Color.parseColor(generalSettings?.header_text_color))
        buttonVideo.setTextColor(Color.parseColor(generalSettings?.header_text_color))

        Desk360CustomStyle.setFontWeight(buttonImage, context, 500)
        Desk360CustomStyle.setFontWeight(buttonPdf, context, 500)
        Desk360CustomStyle.setFontWeight(buttonVideo, context, 500)

        buttonImage.setOnClickListener {
            listener.onButtonClicked(FileType.IMAGE)
            dismiss()
        }
        buttonVideo.setOnClickListener {
            listener.onButtonClicked(FileType.VIDEO)
            dismiss()
        }

        buttonPdf.setOnClickListener {
            listener.onButtonClicked(FileType.DOC)
            dismiss()
        }

        return view
    }

    interface BottomSheetListener {
        fun onButtonClicked(typeOfAttachment: FileType)
    }
}

enum class FileType {
    IMAGE, VIDEO, DOC
}