/*
 * ©Copyright, 2018 Maxim Eliseykin, All right reserved.
 */

package cz.cvut.fs.robduino3;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;

public class ChooseTypeConnectActivity extends Activity {
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_choose_type_of_connect);
        imageButton = findViewById(R.id.imageButton2);
    }

    public void Back(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void nextActivity4(View v) {
        Intent intent = new Intent(this, ControlActivity.class);
        startActivity(intent);
    }
}
