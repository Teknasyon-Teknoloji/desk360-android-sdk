package com.teknasyon.desk360.view.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360SuccessScreenLayoutBinding
import com.teknasyon.desk360.helper.Desk360Constants


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
            Navigation
                .findNavController(it)
                .navigateUp()
        }

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
    }

}
