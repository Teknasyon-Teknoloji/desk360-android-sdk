package com.teknasyon.desk360.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360AddNewTicketLayoutBinding
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.view.adapter.Desk360SupportTypeAdapter
import com.teknasyon.desk360.viewmodel.AddNewTicketViewModel


/**
 * Created by seyfullah on 30,May,2019
 *
 */

open class Desk360AddNewTicketFragment : Fragment() {

    private var viewModel: AddNewTicketViewModel? = null

    private var binding: Desk360AddNewTicketLayoutBinding? = null
    private var typeList: ArrayList<Desk360Type>? = null

    private var selectedTypeId: Int = 1

    private var observer = Observer<ArrayList<Desk360Type>> {
        binding?.loadingProgress?.visibility = View.GONE
        if (it != null) {
            typeList = it

            val listOfType: ArrayList<String> = arrayListOf()
            for (i in 0 until it.size) {
                listOfType.add(it[i].title.toString())
            }
            val myAdapter =
                context?.let { it1 ->
                    Desk360SupportTypeAdapter(
                        it1,
                        R.layout.desk360_type_dropdown,
                        listOfType
                    )
                }
            binding?.subjectType?.adapter = myAdapter
        }
    }

    private var observerName = Observer<Boolean> {
        if (it != null) {
            if (!it) {
                binding?.nameEditText!!.isEnabled = true
                binding?.nameEditText!!.requestFocus()
                binding?.nameEditText!!.onKeyUp(
                    KEYCODE_DPAD_CENTER,
                    KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
                )
            }
        }
    }

    private var observerEMail = Observer<Boolean> {
        it?.let {
            if (!it) {
                binding?.emailEditText!!.isEnabled = true
                binding?.emailEditText!!.requestFocus()
                binding?.emailEditText!!.onKeyUp(
                    KEYCODE_DPAD_CENTER,
                    KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
                )
            }
        }
    }

    private var observerMessage = Observer<Boolean> {
        if (it != null) {
            if (!it) {
                binding?.messageEditText!!.isEnabled = true
                binding?.messageEditText!!.requestFocus()
                binding?.messageEditText?.onKeyUp(
                    KEYCODE_DPAD_UP, KeyEvent(ACTION_UP, KEYCODE_DPAD_UP)
                )
            }
            binding?.messageEditText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding?.baseLayout?.bottom?.let { it1 ->
                        binding?.baseLayout?.scrollTo(
                            0,
                            it1
                        )
                    }
                }
            }
        }
    }

    private var observerAddedTicket = Observer<String> {
        if (it != null) {
            view?.let { it1 ->

                //                RxBus.publish("backButtonActionKey")
//                Navigation
//                    .findNavController(it1)
//                    .navigateUp()
//                Navigation
//                    .findNavController(it1)
//                    .navigateUp()
                Navigation.findNavController(it1)
                    .navigate(R.id.action_addNewTicketFragment_to_thanksFragment,null,NavOptions.Builder().setPopUpTo(R.id.addNewTicketFragment,true).build())

            }

            val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                com.teknasyon.desk360.R.layout.desk360_add_new_ticket_layout,
                container,
                false
            )

        RxBus.publish("transparentBackground")
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = AddNewTicketViewModel()
        viewModel?.typeList?.observe(this, observer)
        viewModel?.nameFieldFill?.observe(this, observerName)
        viewModel?.emailFieldFill?.observe(this, observerEMail)
        viewModel?.messageFieldFill?.observe(this, observerMessage)
        viewModel?.addedTicket?.observe(this, observerAddedTicket)
        binding?.subjectType?.prompt = "Gender"
        binding?.subjectType?.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    typeList?.let { it[position].let { it1 -> selectedTypeId = it1.id!! } }
                }
            })

        binding?.txtBottomFooterMessageForm?.movementMethod = ScrollingMovementMethod()
        binding?.sendButton?.setOnClickListener {
            viewModel?.validateAllField(selectedTypeId)
        }

        binding?.viewModel = viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let {
            with(viewModel!!) {
                typeList?.removeObserver(observer)
                nameFieldFill.removeObserver(observerName)
                emailFieldFill.removeObserver(observerEMail)
//                subjectFieldFill.removeObserver(observerSubject)
                messageFieldFill.removeObserver(observerMessage)
                addedTicket.removeObserver(observerAddedTicket)
            }
        }
    }
}