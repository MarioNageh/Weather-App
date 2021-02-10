package com.musala.app.weather.views.dialogs;


import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.musala.app.weather.databinding.PopupDialogNoInternetConnectionBinding;

public class PopupDialogNoInternetConnection extends BaseDialogFragment {
    INoInternetDialog iNoInternetDialog;
    PopupDialogNoInternetConnectionBinding viewBinding;

    public PopupDialogNoInternetConnection(INoInternetDialog iNoInternetDialog) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, new IBaseDialogFragment() {
            @Override
            public ViewBinding inflateUi(AppCompatActivity appCompatActivity) {
                return PopupDialogNoInternetConnectionBinding.inflate(appCompatActivity.getLayoutInflater());
            }
        }, false);
        this.iNoInternetDialog = iNoInternetDialog;
    }


    @Override
    public void SetListeners() {
        viewBinding = (PopupDialogNoInternetConnectionBinding) getViewBinding();
        viewBinding.tvInternetConnection.setOnClickListener(v -> {
            dismissAllowingStateLoss();
        });
        viewBinding.tvRetry.setOnClickListener(v -> {
            iNoInternetDialog.onResetClicked();
            dismissAllowingStateLoss();
        });
    }

    @Override
    public void StartDialog() {

    }

    public interface INoInternetDialog {
        void onResetClicked();
    }
}
