package com.color.kid.colorpaintkids.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tung on 1/19/2017.
 */

public class TaskFragment extends Fragment implements TaskManager {
    private final Object mLock;
    private List<Runnable> mPendingCallbacks;
    private Boolean mReady;

    public TaskFragment() {
        this.mLock = new Object();
        this.mReady = Boolean.valueOf(false);
        this.mPendingCallbacks = new LinkedList();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        synchronized (this.mLock) {
            this.mReady = Boolean.valueOf(true);
        }
        if (isResumed()) {
            synchronized (this.mLock) {
                int pendingCallbacks = this.mPendingCallbacks.size();
                while (true) {
                    int pendingCallbacks2 = pendingCallbacks - 1;
                    if (pendingCallbacks > 0) {
                        runNow((Runnable) this.mPendingCallbacks.remove(0));
                        pendingCallbacks = pendingCallbacks2;
                    }
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        synchronized (this.mLock) {
            this.mReady = Boolean.valueOf(false);
        }
    }

    public void onDetach() {
        super.onDetach();
    }

    public void runTaskCallback(Runnable runnable) {
        if (this.mReady.booleanValue() && isResumed()) {
            runNow(runnable);
        } else {
            addPending(runnable);
        }
    }

    @SuppressLint({"NewApi"})
    public void executeTask(AsyncTask<Void, ?, ?> task) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {
            task.execute(new Void[0]);
        }
    }

    private void runNow(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

    private void addPending(Runnable runnable) {
        synchronized (this.mLock) {
            this.mPendingCallbacks.add(runnable);
        }
    }
}
