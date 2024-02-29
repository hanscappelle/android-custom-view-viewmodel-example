package be.hcpl.android.customviewvm

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class CustomView : ConstraintLayout, DefaultLifecycleObserver {

    private lateinit var labelView: TextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate(context, R.layout.view_custom, this)
        // makes this view lifecycle aware
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    // retrieve viewModel instance here
    private val viewModel by lazy {
        findViewTreeViewModelStoreOwner()?.let { owner ->
            ViewModelProvider(
                owner,
                // use KoinViewModelFactory here when depending on ctor injection
            )[CustomViewViewModel::class.java]
        }
    }

    // region next hook into the lifecycle methods you need

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        labelView = findViewById(R.id.label)
        labelView.setOnClickListener { viewModel?.onLabelSelected() }
        // observe changes here
        viewModel?.events?.observe(owner) { handleEvent(it) }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModel?.onResume()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    // endregion

    private fun handleEvent(event: CustomViewViewModel.UiEvent) {
        when (event) {
            is CustomViewViewModel.UiEvent.NewLabel -> labelView.text = event.label
        }
    }

}