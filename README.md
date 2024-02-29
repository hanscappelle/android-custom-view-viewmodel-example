# android-custom-view-viewmodel-example

Simple example of how to manage lifecycle on your CustomView including a ViewModel

For starters you'll need a lifecycle aware `Activity` like `AppCompatActivity`to host the 
`CustomView` we will be creating. 

```
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

This has a layout that just includes the custom view

```
<be.hcpl.android.customviewvm.CustomView
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

And then for the `CustomView` implementation you need it to implement `DefaultLifecycleObserver`
and actually register it to observe like so:

```
class CustomView : ConstraintLayout, DefaultLifecycleObserver {

    //...

    init {
        inflate(context, R.layout.view_custom, this)
        // makes this view lifecycle aware
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }
```

For retrieving a `ViewModel` of the type we'll create next you also need to use the 
`findViewTreeViewModelStoreOwner`and provide a `ViewModelProvider`

```
    private val viewModel by lazy {
        findViewTreeViewModelStoreOwner()?.let { owner ->
            ViewModelProvider(
                owner,
                // use KoinViewModelFactory here when depending on ctor injection
            )[CustomViewViewModel::class.java]
        }
    }
```

Then you can register on the `LiveData` to get updates:
```

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        //...
        // observe changes here
        viewModel?.events?.observe(owner) { handleEvent(it) }
    }
```

Finally you'll need a `ViewModel` implementation:

```
class CustomViewViewModel : ViewModel() {

    val events = MutableLiveData<UiEvent>()
    
    //...
}
```