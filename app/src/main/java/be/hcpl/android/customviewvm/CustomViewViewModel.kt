package be.hcpl.android.customviewvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomViewViewModel : ViewModel() {

    val events = MutableLiveData<UiEvent>()

    private fun generateNewLabel() {
        events.value = UiEvent.NewLabel("new label ${System.currentTimeMillis()}")
    }

    fun onLabelSelected() {
        generateNewLabel()
    }

    fun onResume() {
        generateNewLabel()
    }

    sealed class UiEvent {

        data class NewLabel(val label: String) : UiEvent()
    }

}