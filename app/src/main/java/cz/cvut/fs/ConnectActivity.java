/*
 * ©Copyright, 2018 Maxim Eliseykin, All right reserved.
 */

package cz.cvut.fs.robduino3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConnectActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_connect);
    }

    public void gonext2(View view) {
        Intent intent = new Intent(this, ChooseTypeConnectActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}