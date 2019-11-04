package com.teknasyon.desk360.view.fragment

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
import com.teknasyon.desk360.view.activity.Desk360BaseActivity


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
        binding.emptysAddNewTicketButton.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(
                    R.id.action_preNewTicketFragment_to_addNewTicketFragment, null,
                    NavOptions.Builder().setPopUpTo(R.id.preNewTicketFragment, true).build()
                )
        }

        binding.txtBottomFooterTicket.movementMethod = ScrollingMovementMethod()
    }

}
