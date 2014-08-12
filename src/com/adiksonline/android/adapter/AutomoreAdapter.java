package com.adiksonline.android.adapter;

import java.util.concurrent.atomic.AtomicBoolean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.commonsware.cwac.adapter.AdapterWrapper;

public class AutomoreAdapter extends AdapterWrapper {

    public static interface AutomoreListener {
        public void onMoreRequested(final AutomoreAdapter adapter);
    }

    private AtomicBoolean automoreEnabled = new AtomicBoolean(true);
    private AtomicBoolean moreLoading = new AtomicBoolean(false);
    private Context context;
    private int offset = 0;
    private View pendingView;
    private AutomoreListener automoreListener;
    


    /**
     * Constructor wrapping a supplied ListAdapter.
     * 
     * @param context
     * @param wrapped
     */
    public AutomoreAdapter(Context context, ListAdapter wrapped) {
        this(context, wrapped, R.layout.item_automore, true);
    }

    /**
     * Constructor wrapping a supplied ListAdapter and explicitly set if there
     * is more data that needs to be fetched or not.
     * 
     * @param context
     * @param wrapped
     * @param automoreEnabled
     */
    public AutomoreAdapter(Context context, ListAdapter wrapped, boolean automoreEnabled) {
        this(context, wrapped, R.layout.item_automore, automoreEnabled);
    }

    /**
     * Constructor wrapping a supplied ListAdapter and providing a id for a
     * pending view.
     * 
     * @param context
     * @param wrapped
     * @param pendingResource
     */
    public AutomoreAdapter(Context context, ListAdapter wrapped, int pendingResource) {
        this(context, wrapped, pendingResource, true);
    }

    /**
     * Constructor wrapping a supplied ListAdapter, providing a id for a pending
     * view and explicitly set if there is more data that needs to be fetched or
     * not.
     * 
     * @param context
     * @param wrapped
     * @param pendingResource
     * @param automoreEnabled
     */
    public AutomoreAdapter(Context context, ListAdapter wrapped, int pendingResource, boolean automoreEnabled) {
        super(wrapped);
        this.context = context;
        pendingView = createPendingView(pendingResource, null);
        this.setAutomoreEnabled(automoreEnabled);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     */
    @Override
    public int getCount() {
        if (automoreEnabled.get()) {
            return (super.getCount() + 1); // one more for "pending"
        }

        return (super.getCount());
    }

    /**
     * Masks ViewType so the AdapterView replaces the "Pending" row when new
     * data is loaded.
     */
    public int getItemViewType(int position) {
        if (position == getWrappedAdapter().getCount()) {
            return (IGNORE_ITEM_VIEW_TYPE);
        }

        return (super.getItemViewType(position));
    }

    /**
     * Masks ViewType so the AdapterView replaces the "Pending" row when new
     * data is loaded.
     * 
     * @see #getItemViewType(int)
     */
    public int getViewTypeCount() {
        return (super.getViewTypeCount() + 1);
    }

    @Override
    public Object getItem(int position) {
        if (position >= super.getCount()) {
            return (null);
        }

        return (super.getItem(position));
    }

    @Override
    public boolean areAllItemsEnabled() {
        return (false);
    }

    @Override
    public boolean isEnabled(int position) {
        if (position >= super.getCount()) {
            return (false);
        }

        return (super.isEnabled(position));
    }

    /**
     * Get a View that displays the data at the specified position in the data
     * set. In this case, if we are at the end of the list and we are still in
     * append mode, we ask for a pending view and return it, plus kick off the
     * background task to append more data to the wrapped adapter.
     * 
     * @param position
     *            Position of the item whose data we want
     * @param convertView
     *            View to recycle, if not null
     * @param parent
     *            ViewGroup containing the returned View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!moreLoading.get() && !super.isEmpty() && position + offset >= super.getCount() && automoreListener != null) {
            moreLoading.set(true);
            automoreListener.onMoreRequested(this);
        }
        if (automoreEnabled.get() && position == super.getCount()) {
            return pendingView;
        }
        return (super.getView(position, convertView, parent));
    }

    /**
     * Notifies the adapter that loading has completed and the listview can now
     * be refreshed. Call this method once the more data has been fully loaded.
     * 
     */
    public void setMoreCompleted() {
        moreLoading.set(false);
        notifyDataSetChanged();
    }

    /**
     * Hides/Shows the automore pending view. Calling this method with false
     * could be needed when there are no more data to load, in which case, the
     * pending view need not be visible anymore. This also disables the
     * onMoreRequested trigger. Calling it again with true re-enables the
     * pending view, as well as the onMoreRequested trigger. Calling it
     * repeatedly with the same value has no effect however.
     * 
     * @param newValue
     */
    public void setAutomoreEnabled(boolean newValue) {
        boolean same = (newValue == automoreEnabled.get());
        automoreEnabled.set(newValue);

        if (!same) {
            notifyDataSetChanged();
        }
    }

    /**
     * Inflates pending view using the given res ID passed into the constructor
     * 
     * @param resId
     * @param parent
     * @return inflated view.
     */
    protected View createPendingView(int resId, ViewGroup parent) {
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(resId, parent, false);
        }

        throw new RuntimeException("Supply valid context and resource in constructor or override createPendingView()");
    }

    public AutomoreListener getAutomoreListener() {
        return automoreListener;
    }

    public void setAutomoreListener(AutomoreListener autoMoreListener) {
        this.automoreListener = autoMoreListener;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * Sets which view from the list from whence the onMoreRequested() event
     * could be triggered.
     * 
     * @param offset
     *            number of views before the last
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

}
