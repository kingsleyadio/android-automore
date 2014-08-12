android-automore
================

An adapter wrapper that provides auto load more feature
It is non intrusive by design, making it extremely easy to integrate into existing projects.

### Usage
This is pretty straight forward. Simply wrap your adapter, 
set the automore listener, and modify your view so that it points 
to this adapter instead.

```java
AutomoreAdapter automoreAdapter = new AutomoreAdapter(context, mExistingAdapter);
automoreAdapter.setAutomoreListener(new AutomoreListener() {
    public void onMoreRequested(AutomoreAdapter adapter) {
        // -- load more data into your list --
        adapter.setMoreCompleted();  // notify the adapter that you're done loading
    }
});
mListView.setAdapter(automoreAdapter);
```

In a case where there is no more data to load, you can disable the automore feature 
by simply calling:
```java
automoreAdapter.setAutomoreEnabled(false);
```

### Customizations
It is possible to customize the behaviour of the adapter. Basically, you can:
* **Provide initial layout as well as set automore feature availability**
    * You can set your own custom view by specifying a layout resource. The library however provides a default indeterminate progressbar that is rendered when none is specified.
    * Set whether to initially enable the automore feature or not. Automore is enabled by default
```java
AutomoreAdapter automoreAdapter = new AutomoreAdapter(context, mExistingAdapter, layoutID);
AutomoreAdapter automoreAdapter = new AutomoreAdapter(context, mExistingAdapter, shouldEnableAutomore);
AutomoreAdapter automoreAdapter = new AutomoreAdapter(context, mExistingAdapter, layoutID, shouldEnableAutomore);
```
* **Set the offset value**: This tells the adapter that it is okay to trigger the `onMoreRequested` from any `offset` rows before the last row. This is very useful if you want to start preloading data some few rows before the user scrolls to the end. The offset value is `0` by default. i.e you have to get to the bottom of the list before the `onMoreRequested` is triggered.
```java
automoreAdapter.setOffset(offset);
```

### Dependencies
This project relies upon the 
[CWAC AdapterWrapper](https://github.com/commonsguy/cwac-adapter) project

This project should work on API Level 4 and higher, except for any portions 
that may be noted otherwise in this document. Please report bugs if you find 
features that do not work on API Level 4 and are not noted as requiring a 
higher version.
