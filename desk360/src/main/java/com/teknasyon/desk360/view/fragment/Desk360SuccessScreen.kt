package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360SuccessScreenLayoutBinding
import com.teknasyon.desk360.helper.Desk360SDK
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.view.activity.Desk360BaseActivity

class Desk360SuccessScreen : Fragment() {
    private var binding: Desk360SuccessScreenLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Desk360SuccessScreenLayoutBinding.inflate(inflater, container, false).also {
            binding = it
            binding?.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as Desk360BaseActivity).binding.contactUsMainBottomBar.visibility = View.VISIBLE

        binding?.apply {
            successScreenOpenMessageForm.setOnClickListener {
                findNavController().navigate(Desk360SuccessScreenDirections.actionThanksFragmentToTicketListFragment())
            }

            Desk360CustomStyle.setFontWeight(
                successScreenBottomFooter,
                context,
                Desk360SDK.config?.data?.general_settings?.bottom_note_font_weight
            )
            Desk360CustomStyle.setFontWeight(
                successScreenOpenMessageFormText,
                context,
                Desk360SDK.config?.data?.ticket_success_screen?.button_text_font_weight
            )
            Desk360CustomStyle.setFontWeight(
                successScreenSubtitle,
                context,
                Desk360SDK.config?.data?.ticket_success_screen?.sub_title_font_weight
            )
            Desk360CustomStyle.setFontWeight(
                successScreenDescription,
                context,
                Desk360SDK.config?.data?.ticket_success_screen?.description_font_weight
            )
            Desk360CustomStyle.setStyle(
                Desk360SDK.config?.data?.ticket_success_screen?.button_style_id,
                successScreenOpenMessageForm,
                context!!
            )

            imageReceived.layoutParams?.height = context?.let {
                convertDpToPixel(
                    (Desk360SDK.config?.data?.ticket_success_screen?.icon_size)?.toFloat()!!,
                    it
                ).toInt()
            }

            imageReceived.layoutParams?.width = context?.let {
                convertDpToPixel(
                    (Desk360SDK.config?.data?.ticket_success_screen?.icon_size)?.toFloat()!!,
                    it
                ).toInt()
            }

            imageReceived.requestLayout()
            imageReceived.setImageResource(R.drawable.received_message_image)
            imageReceived.setColorFilter(
                Color.parseColor(Desk360SDK.config?.data?.ticket_success_screen?.icon_color),
                PorterDuff.Mode.SRC_ATOP
            )

            successScreenButtonIcon.setImageResource(R.drawable.zarf)
            successScreenButtonIcon.setColorFilter(
                Color.parseColor(Desk360SDK.config?.data?.ticket_success_screen?.button_text_color),
                PorterDuff.Mode.SRC_ATOP
            )

            successScreenBottomFooter.movementMethod = ScrollingMovementMethod()
            successScreenOpenMessageFormText.text =
                Desk360SDK.config?.data?.ticket_success_screen?.button_text
            successScreenOpenMessageFormText.textSize =
                Desk360SDK.config?.data?.ticket_success_screen?.button_text_font_size!!.toFloat()
            successScreenOpenMessageFormText.setTextColor(Color.parseColor(Desk360SDK.config?.data?.ticket_success_screen?.button_text_color))

            if (Desk360SDK.config?.data?.ticket_success_screen?.button_icon_is_hidden!!) {

                val layoutParams =
                    successScreenOpenMessageFormText.layoutParams as RelativeLayout.LayoutParams
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                successScreenOpenMessageFormText.layoutParams = layoutParams
            }
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
