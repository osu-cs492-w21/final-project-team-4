package com.example.android.steamnews.data;

// Callbacks to perform after certain database actions
public interface OnDatabaseActionCompleteCallback {
    void onSuccess();
    void onFailure(Throwable throwable);
}
