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
        // load more data into your list
        // call setMoreCompleted to notify the adapter that you're through
        adapter.setMoreCompleted();
    }
});
mListView.setAdapter(automoreAdapter);
```

### Dependencies
This project relies upon the 
[CWAC AdapterWrapper](https://github.com/commonsguy/cwac-adapter) project

This project should work on API Level 4 and higher, except for any portions 
that may be noted otherwise in this document. Please report bugs if you find 
features that do not work on API Level 4 and are not noted as requiring a 
higher version.
