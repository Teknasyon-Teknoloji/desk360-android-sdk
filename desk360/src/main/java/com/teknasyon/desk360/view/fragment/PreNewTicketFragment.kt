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
import androidx.navigation.Navigation
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentPreNewTicketBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import kotlinx.android.synthetic.main.desk360_fragment_main.*


class PreNewTicketFragment : Fragment() {
    private lateinit var binding: FragmentPreNewTicketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentPreNewTicketBinding.inflate(inflater, container, false).also {
            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.preScreenButton.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(
                    R.id.action_preNewTicketFragment_to_addNewTicketFragment, null,
                    NavOptions.Builder().setPopUpTo(R.id.preNewTicketFragment, true).build()
                )
        }

        (activity as Desk360BaseActivity).contactUsMainBottomBar.visibility = View.VISIBLE
        (activity as Desk360BaseActivity).changeMainUI()

        Desk360CustomStyle.setFontWeight(
            binding.preScreenButtonText,
            context,
            Desk360Constants.currentType?.data?.create_pre_screen?.button_text_font_weight
        )

        Desk360CustomStyle.setFontWeight(
            binding.preScreenDesc,
            context,
            Desk360Constants.currentType?.data?.create_pre_screen?.description_font_weight
        )
        Desk360CustomStyle.setFontWeight(
            binding.subTitlePreScreen,
            context,
            Desk360Constants.currentType?.data?.create_pre_screen?.sub_title_font_weight
        )

        Desk360CustomStyle.setStyle(
            Desk360Constants.currentType?.data?.create_pre_screen?.button_style_id,
            binding.preScreenButton,
            context!!
        )
        binding.preScreennButtonIcon.setImageResource(R.drawable.zarf)
        binding.preScreennButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.create_pre_screen?.button_text_color),
            PorterDuff.Mode.SRC_ATOP
        )

        Desk360CustomStyle.setFontWeight(
            binding.txtBottomFooterPreScreen,
            context,
            Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight
        )
        binding.txtBottomFooterPreScreen.movementMethod = ScrollingMovementMethod()

    }

}
