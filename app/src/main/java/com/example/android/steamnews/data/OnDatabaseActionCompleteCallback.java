package com.example.android.steamnews.data;

public interface OnDatabaseActionCompleteCallback {
    void onSuccess();
    void onFailure(Throwable throwable);
}
