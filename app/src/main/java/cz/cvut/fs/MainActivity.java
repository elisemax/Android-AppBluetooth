/*
 * ©Copyright 2018 Maxim Eliseykin, All right reserved.
 */

package cz.cvut.fs.robduino3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotonext(View v) {
        Intent intent = new Intent(this, ConnectActivity.class);
        startActivity(intent);
    }
//    public void Greatest(View view){
//        Toast toast=Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.show();
//    }
}
