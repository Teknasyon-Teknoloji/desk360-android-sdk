package com.teknasyon.desk360.view.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentPreNewTicketBinding
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.Desk360SDK
import com.teknasyon.desk360.view.activity.Desk360BaseActivity

class PreNewTicketFragment : Fragment() {
    private var binding: FragmentPreNewTicketBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentPreNewTicketBinding.inflate(inflater, container, false).also {
            binding = it
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as Desk360BaseActivity).binding.contactUsMainBottomBar.visibility = View.VISIBLE
        (activity as Desk360BaseActivity).changeMainUI()

        binding?.apply {
            preScreenButton.setOnClickListener {
                findNavController().navigate(
                    PreNewTicketFragmentDirections.actionPreNewTicketFragmentToAddNewTicketFragment(
                        null
                    ),
                    NavOptions.Builder().setPopUpTo(R.id.preNewTicketFragment, true).build()
                )
            }

            Desk360CustomStyle.setFontWeight(
                preScreenButtonText,
                context,
                Desk360SDK.config?.data?.create_pre_screen?.button_text_font_weight
            )

            Desk360CustomStyle.setFontWeight(
                preScreenDesc,
                context,
                Desk360SDK.config?.data?.create_pre_screen?.description_font_weight
            )
            Desk360CustomStyle.setFontWeight(
                subTitlePreScreen,
                context,
                Desk360SDK.config?.data?.create_pre_screen?.sub_title_font_weight
            )

            Desk360CustomStyle.setStyle(
                Desk360SDK.config?.data?.create_pre_screen?.button_style_id,
                preScreenButton,
                context!!
            )
            preScreennButtonIcon.setImageResource(R.drawable.zarf)
            preScreennButtonIcon.setColorFilter(
                Color.parseColor(Desk360SDK.config?.data?.create_pre_screen?.button_text_color),
                PorterDuff.Mode.SRC_ATOP
            )

            Desk360CustomStyle.setFontWeight(
                txtBottomFooterPreScreen,
                context,
                Desk360SDK.config?.data?.general_settings?.bottom_note_font_weight
            )
            txtBottomFooterPreScreen.movementMethod = ScrollingMovementMethod()
        }
    }

}
