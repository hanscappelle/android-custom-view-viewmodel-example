package be.hcpl.android.customviewvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomViewViewModel : ViewModel() {

    val events = MutableLiveData<UiEvent>()

    private fun generateNewLabel() {
        events.value = UiEvent.NewLabel("new label ${System.currentTimeMillis()}")
    }

    private fun storePreviousLabel() {
        events.value = UiEvent.KeepPreviousLabel
    }

    fun onLabelSelected() {
        storePreviousLabel()
        generateNewLabel()
    }

    fun onResume() {
        generateNewLabel()
    }

    fun onPause() {
        storePreviousLabel()
    }

    sealed class UiEvent {
        data class NewLabel(val label: String) : UiEvent()
        object KeepPreviousLabel : UiEvent()
    }

}