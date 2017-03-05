package com.example.mayank.singleboom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignOutActivity extends AppCompatActivity {
    SessionManagement session;
//    public Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      setContentView(R.layout.activity_main);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();


        AlertDialog.Builder ab = new AlertDialog.Builder(SignOutActivity.this);
        ab.setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     //   finish();

                        session.logoutUser();
                        Intent ii =new Intent(SignOutActivity.this,ChooseActivity.class);
                        startActivity(ii);
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
