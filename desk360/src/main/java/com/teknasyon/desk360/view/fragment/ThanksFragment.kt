package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.teknasyon.desk360.databinding.Desk360ReceivedMessageLayoutBinding


class ThanksFragment : Fragment() {
    private lateinit var binding: Desk360ReceivedMessageLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Desk360ReceivedMessageLayoutBinding.inflate(inflater, container, false).also {
            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOpenMessageForm.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigateUp()
        }

        binding.txtBottomFooter.movementMethod = ScrollingMovementMethod()
    }

}
