package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.content.Intent
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
import androidx.navigation.Navigation
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360SuccessScreenLayoutBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import kotlinx.android.synthetic.main.desk360_fragment_main.*


class Desk360SuccessScreen : Fragment() {

    private lateinit var binding: Desk360SuccessScreenLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Desk360SuccessScreenLayoutBinding.inflate(inflater, container, false).also {
            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.successScreenOpenMessageForm.setOnClickListener {

//            Navigation.findNavController(it).navigateUp()
            Navigation.findNavController(activity!!,R.id.my_nav_host_fragment).navigate(R.id.action_thanksFragment_to_ticketListFragment)
        }

        (activity as Desk360BaseActivity).contactUsMainBottomBar.visibility = View.VISIBLE


        Desk360CustomStyle.setFontWeight(
            binding.successScreenBottomFooter,
            context,
            Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight
        )
        Desk360CustomStyle.setFontWeight(
            binding.successScreenOpenMessageFormText,
            context,
            Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_font_weight
        )
        Desk360CustomStyle.setFontWeight(
            binding.successScreenSubtitle,
            context,
            Desk360Constants.currentType?.data?.ticket_success_screen?.sub_title_font_weight
        )
        Desk360CustomStyle.setFontWeight(
            binding.successScreenDescription,
            context,
            Desk360Constants.currentType?.data?.ticket_success_screen?.description_font_weight
        )
        Desk360CustomStyle.setStyle(
            Desk360Constants.currentType?.data?.ticket_success_screen?.button_style_id,
            binding.successScreenOpenMessageForm,
            context!!
        )

        binding.imageReceived.layoutParams?.height = context?.let {
            convertDpToPixel(
                (Desk360Constants.currentType?.data?.ticket_success_screen?.icon_size)?.toFloat()!!,
                it
            ).toInt()
        }
        binding.imageReceived.layoutParams?.width = context?.let {
            convertDpToPixel(
                (Desk360Constants.currentType?.data?.ticket_success_screen?.icon_size)?.toFloat()!!,
                it
            ).toInt()
        }
        binding.imageReceived.requestLayout()

        binding.imageReceived.setImageResource(R.drawable.received_message_image)
        binding.imageReceived.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_success_screen?.icon_color),
            PorterDuff.Mode.SRC_ATOP
        )

        binding.successScreenButtonIcon.setImageResource(R.drawable.zarf)
        binding.successScreenButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_color),
            PorterDuff.Mode.SRC_ATOP
        )

        binding.successScreenBottomFooter.movementMethod = ScrollingMovementMethod()

        binding.successScreenOpenMessageFormText.text = Desk360Constants.currentType?.data?.ticket_success_screen?.button_text
        binding.successScreenOpenMessageFormText.textSize = Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_font_size!!.toFloat()
        binding.successScreenOpenMessageFormText.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_color))

        if (Desk360Constants.currentType?.data?.ticket_success_screen?.button_icon_is_hidden!!) {

            val layoutParams = binding.successScreenOpenMessageFormText.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            binding.successScreenOpenMessageFormText.layoutParams = layoutParams
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


}
