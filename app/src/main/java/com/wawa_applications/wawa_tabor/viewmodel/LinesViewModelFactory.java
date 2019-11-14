package com.wawa_applications.wawa_tabor.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LinesViewModelFactory implements ViewModelProvider.Factory {

    private String apiKey;

    public LinesViewModelFactory(String apiKey) {
        this.apiKey = apiKey;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LinesViewModel(apiKey);
    }
}
