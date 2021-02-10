package com.musala.app.weather.views.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;


import com.musala.app.weather.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class BaseDialogFragment extends DialogFragment {
    boolean mIsStateAlreadySaved = false;
    boolean mPendingShowDialog = false;
    boolean istTime = false;
    boolean isCancelable = false;
    private ViewBinding viewBinding;
    public AppCompatActivity activity;
    public Dialog dialog;
    int width = ViewGroup.LayoutParams.MATCH_PARENT;
    int height = ViewGroup.LayoutParams.MATCH_PARENT;
    IBaseDialogFragment iBaseDialogFragment;

    public void show(FragmentManager fragmentManager, String str) {
        try {
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add((Fragment) this, str);
            beginTransaction.commitAllowingStateLoss();
        } catch (Throwable e) {
        }
    }

    public void show(AppCompatActivity activity) {
        try {
            FragmentTransaction beginTransaction = activity.getSupportFragmentManager().beginTransaction();
            beginTransaction.add((Fragment) this, this.getClass().getSimpleName());
            beginTransaction.commitAllowingStateLoss();
        } catch (Throwable e) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onResume() {
        if (!istTime) {
            StartDialog();
            istTime = true;
        }

        this.mIsStateAlreadySaved = false;
        onResumeFragments();
        super.onResume();
    }

    public void onResumeFragments() {
        if (this.mPendingShowDialog) {
            this.mPendingShowDialog = false;
            showDialog();
        }
    }

    public void onPause() {
        this.mIsStateAlreadySaved = true;
        super.onPause();
    }

    private void showDialog() {

        if (this.mIsStateAlreadySaved) {
            this.mPendingShowDialog = true;
            return;
        }
    }

    public Dialog handelDialog(AppCompatActivity activity) {
        dialog = new Dialog(activity);
        setViewBinding(iBaseDialogFragment.inflateUi(activity));
        dialog.requestWindowFeature(1);
        dialog.setContentView(getViewBinding().getRoot());
        dialog.getWindow().getAttributes().windowAnimations = R.anim.fade_in;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor
                (activity, R.color.transparent)));
        dialog.getWindow().setSoftInputMode(STYLE_NO_INPUT);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(isCancelable);
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
        return dialog;
    }


    //ViewGroup.LayoutParams.MATCH_PARENT

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT})
    public @interface LayoutParams {
    }

    public BaseDialogFragment(@LayoutParams int width, @LayoutParams int height, IBaseDialogFragment i, boolean isCancelable) {
        iBaseDialogFragment = i;
        setWidth(width);
        setHeight(height);
        setCancelable(isCancelable);

    }


    public BaseDialogFragment(@LayoutParams int width, @LayoutParams int height, boolean isCancelable) {

        setWidth(width);
        setHeight(height);
        setCancelable(isCancelable);
    }

    public BaseDialogFragment() {

    }


    public String string(int message, Context context) {
        return context.getResources().getString(message);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (activity == null)
            activity = (AppCompatActivity) getActivity();

        dialog = handelDialog(activity);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        SetListeners();
        super.onActivityCreated(savedInstanceState);
    }


    public boolean isCancelable() {
        return isCancelable;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
        }
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public abstract void SetListeners();

    public abstract void StartDialog();

    public ViewBinding getViewBinding() {
        return viewBinding;
    }

    public void setViewBinding(ViewBinding viewBinding) {
        this.viewBinding = viewBinding;
    }


    public View findViewById(int id) {
        return dialog.findViewById(id);
    }

    public interface IBaseDialogFragment {
        ViewBinding inflateUi(AppCompatActivity appCompatActivity);
    }

    @Override
    public void dismissAllowingStateLoss() {

        super.dismissAllowingStateLoss();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}
