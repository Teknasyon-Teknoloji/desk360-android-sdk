package com.teknasyon.desk360.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.BR
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.model.NewSupportResponse
import com.teknasyon.desk360.model.TicketReq
import com.teknasyon.desk360.model.Type
import com.teknasyon.desk360.model.TypeResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.regex.Pattern

/**
 * Created by seyfullah on 30,May,2019
 *
 */

class AddNewTicketViewModel : ViewModel() {
    var typeList: MutableLiveData<ArrayList<Type>>? = MutableLiveData()
    var addedTicket: MutableLiveData<String> = MutableLiveData()
    private val ticketItem = TicketReq()

    var observable = NewSupportObservable()
    val nameFieldFill: MutableLiveData<Boolean> = MutableLiveData()
    val emailFieldFill: MutableLiveData<Boolean> = MutableLiveData()
    var subjectFieldFill: MutableLiveData<Boolean> = MutableLiveData()
    var messageFieldFill: MutableLiveData<Boolean> = MutableLiveData()

    var nameData: String? = null
    var emailData: String? = null
    var subjectData: String? = null
    var messageData: String? = null
    var messageLength: Int = 0

    init {
        messageFieldFill.postValue(false)
        subjectFieldFill.postValue(false)
        emailFieldFill.postValue(false)
        nameFieldFill.postValue(false)
        GlobalScope.launch {
            delay(300)
            getTypeList()
        }
    }

    private fun getTypeList() {
        RetrofitFactory.instance.httpService.getTypeList()
            .enqueue(object : BaseCallback<TypeResponse>() {
                override fun onResponseSuccess(
                    call: Call<TypeResponse>,
                    response: Response<TypeResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        typeList?.value = response.body()!!.data
                    } else {
                        typeList?.value = null
                    }
                }
            })
    }

    private fun addSupportTicket() {
        RetrofitFactory.instance.httpService.addTicket(ticketItem)
            .enqueue(object : BaseCallback<NewSupportResponse>() {
                override fun onResponseSuccess(
                    call: Call<NewSupportResponse>,
                    response: Response<NewSupportResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        addedTicket.value =
                            response.body()!!.data?.name //todo işlem bittiğinde ticket ana sayfasına dönecek
                    } else {
                        addedTicket.value = null
                    }
                }
            })
    }

    fun validateAllField(selectedTypeId: Int) {
        if (nameFieldFill.value == true && emailFieldFill.value == true && subjectFieldFill.value == true && messageLength > 0) {
            ticketItem.email = emailData
            ticketItem.name = nameData
            ticketItem.subject = subjectData
            ticketItem.message = messageData
            ticketItem.type_id = selectedTypeId.toString()
            ticketItem.source = "App"
            ticketItem.platform = "Android"
            ticketItem.country_code = Desk360Constants.countryCode()
            addSupportTicket()
        } else {
            when {
                nameFieldFill.value == false -> nameFieldFill.postValue(false)
                emailFieldFill.value == false -> emailFieldFill.postValue(false)
                subjectFieldFill.value == false -> subjectFieldFill.postValue(false)
                messageLength <= 0 -> messageFieldFill.postValue(false)
            }
        }
    }

    inner class NewSupportObservable : BaseObservable() {
        val nameHintText: String?
            @Bindable
            get() = "İsim Soyisim"

        val mailHintText: String?
            @Bindable
            get() = "E-Mail"

        val subjectHintText: String?
            @Bindable
            get() = "Subject"

        val messageHintText: String?
            @Bindable
            get() = "Mesajınız"

        val messageLengthData: String?
            @Bindable
            get() = "$messageLength/120"

        fun nameQuality(s: CharSequence) {
            nameData = s.toString()
            nameFieldFill.postValue(
                when {
                    s.isEmpty() -> {
                        false
                    }
                    else -> {
                        true
                    }
                }
            )
        }

        fun emailQuality(s: CharSequence) {
            emailData = s.toString()
            emailFieldFill.postValue(
                when {
                    s.isEmpty() -> false
                    !checkEmail(email = s.toString()) -> false
                    else -> {
                        true
                    }
                }
            )
        }

        fun subjectQuality(s: CharSequence) {
            subjectData = s.toString()
            subjectFieldFill.postValue(
                when {
                    s.isEmpty() -> false
                    else -> {
                        true
                    }
                }
            )
        }

        fun messageQuality(s: CharSequence) {
            messageData = s.toString()
            messageLength = messageData!!.length
            notifyPropertyChanged(BR.messageLengthData)
            messageFieldFill.postValue(
                when {
                    s.isEmpty() -> false
                    else -> {
                        true
                    }
                }
            )
        }

        private fun checkEmail(email: String): Boolean {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        }


        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }
}