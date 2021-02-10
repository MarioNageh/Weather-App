package com.musala.app.weather.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.musala.app.weather.R;
import com.musala.app.weather.views.dialogs.PopupDialogNoInternetConnection;

public abstract class BaseActivity extends AppCompatActivity {
    private IPermissions iPermissions;
    private String[] defaultPermissionArray;
    // My Year That I Was Born :)
    private final int REQUEST_PERMISSION = 1997;

    PopupDialogNoInternetConnection popupDialogNoInternetConnection;
    public void showInternetErrorDialog(PopupDialogNoInternetConnection.INoInternetDialog iNoInternetDialog) {
         popupDialogNoInternetConnection = new PopupDialogNoInternetConnection(iNoInternetDialog);
        popupDialogNoInternetConnection.show(this);
    }
    public void dismisInternetDialog(){
        if (popupDialogNoInternetConnection!=null||popupDialogNoInternetConnection.isVisible()){
            popupDialogNoInternetConnection.dismissAllowingStateLoss();
        }
    }


    public interface IPermissions {
        void OnPermissionRequestedDone(boolean b);
    }

    public void setPermissions(IPermissions iPermissions) {
        this.iPermissions = iPermissions;
    }

    public void setDefaultPermissionArray(String[] defaultPermissionArray) {
        this.defaultPermissionArray = defaultPermissionArray;
    }

    public boolean CheckPermissionList() {
        for (String permission : defaultPermissionArray) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        iPermissions.OnPermissionRequestedDone(true);
        return true;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, defaultPermissionArray,
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        iPermissions.OnPermissionRequestedDone(false);
                        return;
                    }
                }
                iPermissions.OnPermissionRequestedDone(true);

                break;
            }
            default: {
                break;
            }

        }
    }
    public interface IDialog{
        void onOkPressed();
    }
    public void showErrorDialog(IDialog iDialog,int title,int message) {
        new AwesomeErrorDialog(this)
                .setTitle(title)
                .setMessage(message)
                .setColoredCircle(R.color.red)
                .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                .setCancelable(true)
                .setButtonText(getString(R.string.dialog_ok_button))
                .setButtonBackgroundColor(R.color.red)
                .setButtonText(getString(R.string.dialog_ok_button))
                .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        iDialog.onOkPressed();
                    }
                })
                .show();
    }
    public void showErrorDialog(IDialog iDialog,String title,String message) {
        new AwesomeErrorDialog(this)
                .setTitle(title)
                .setMessage(message)
                .setColoredCircle(R.color.red)
                .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                .setCancelable(true)
                .setButtonText(getString(R.string.dialog_ok_button))
                .setButtonBackgroundColor(R.color.red)
                .setButtonText(getString(R.string.dialog_ok_button))
                .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        iDialog.onOkPressed();
                    }
                })
                .show();
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_from_left_to_right);

    }
}
