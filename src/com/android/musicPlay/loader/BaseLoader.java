package com.android.musicPlay.loader;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class BaseLoader<T> extends AsyncTaskLoader<List<T>> {

    public List<T> mApps;

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    public List<T> loadInBackground() {
        return null;
    }

    /**
     * Called when there is new data to deliver to the client. The super class
     * will take care of delivering it; the implementation here just adds a
     * little more logic.
     */
    @Override
    public void deliverResult(List<T> apps) {
        if (isReset()) {

        }
        if (isReset()) {
            // An async query came in while the loader is stopped. We
            // don't need the result.
            if (apps != null) {
                onReleaseResources(apps);
            }
        }
        List<T> oldApps = apps;
        mApps = apps;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(apps);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldApps != null) {
            onReleaseResources(oldApps);
        }

    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        // TODO Auto-generated method stub
        if (mApps != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mApps);
        } else {
            // If the data is not currently available, start a load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // TODO Auto-generated method stub
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(List<T> data) {
        // TODO Auto-generated method stub
        super.onCanceled(data);
        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(data);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mApps != null) {
            onReleaseResources(mApps);
            mApps = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated with an
     * actively loaded data set.
     */
    protected void onReleaseResources(List<T> apps) {
        // For a simple List<> there is nothing to do. For something
        // like a Cursor, we would close it here.
    }
}
