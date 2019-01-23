/*
 * ©Copyright, 2018 Maxim Eliseykin, All right reserved.
 */
package cz.cvut.fs.robduino3;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class ControlActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    private boolean isLeftImageButton = false;
    private boolean isRightImageButton = false;
    private boolean isUpImageButton = false;
    private boolean isDownImageButton = false;
    private int REQUEST_ENABLE_BLUETOOTH = 1001;
    private ImageButton leftImageButton;
    private ImageButton rightImageButton;
    private ImageButton upImageButton;
    private ImageButton downImageButton;
    private BluetoothAdapter myBluetoothAdapter;
    private ProgressDialog myProgressBar;
    private ListView listDevices;
    private BluetoothSocket myBluetoothSocket;
    private OutputStream myOutputStream;
    private ArrayList<BluetoothDevice> myDevices = new ArrayList<>();
    private DeviceList myDeviceList;

    private final View.OnTouchListener touchListener =new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            String command="";
            switch (event.getAction()){
                //Buttons pressed condition
                case MotionEvent.ACTION_DOWN:
                    if(v.equals(leftImageButton)){
                        command = isLeftImageButton ? "l" : "s";
                        Log.d(TAG, "OnClick: isLeftImageButton = " + isLeftImageButton);
                    }
                    if (v.equals(rightImageButton)) {
                        isRightImageButton = !isRightImageButton;
                        command = isRightImageButton ? "r" : "s";
                        Log.d(TAG, "OnClick: isRightImageButton = " + isRightImageButton);
                    }
                    //Button for going up
                    if (v.equals(upImageButton)) {
                        isUpImageButton = !isUpImageButton;
                        command = isUpImageButton ? "u" : "s";
                        Log.d(TAG, "OnClick: isDownImageButton = " + isDownImageButton);
                    }
                    //Button for going down
                    if (v.equals(downImageButton)) {
                        isDownImageButton = !isDownImageButton;
                        command = isDownImageButton ? "d" : "s";
                        Log.d(TAG, "OnClick: isDownImageButton = " + isDownImageButton);
                    }
                    setMessage(command);
                    return true;

                //Buttons up condition
                case MotionEvent.ACTION_UP:
                    command= "s";
                    setMessage(command);
                    return true;
            }
            return false;
        }
    };


    //    Monitoring bluetooth status
//    On / Off and search for new devices
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            //start new searching
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Log.d(TAG, "onReceiver : ACTION_DISCOVERY_STARTED");
                showMessage("Start searching devices");
                myProgressBar = ProgressDialog.show(ControlActivity.this, "Searching Devices", "Please wait");
            }
            //search ended
            if (action.equals((BluetoothAdapter.ACTION_DISCOVERY_FINISHED))) {
                Log.d(TAG, "onReceiver : ACTION_DISCOVERY_FINISHED");
                myProgressBar.dismiss();
                showMessage("Search end");
                showListDevices();
            }
            //if searched new device
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                Log.d(TAG, "onReceiver : ACTION_FOUND");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    if (!myDevices.contains(device)) {
                        myDeviceList.add(device);
                    }

                }

            }
        }
    };
    //organize the element in the device list and getter for devices item
    private AdapterView.OnItemClickListener itemOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            BluetoothDevice device = myDevices.get(position);

            startConnection(device);
        }
    };

    @Override
    protected void onDestroy() {

        //voiding memory leaks by closing ports in the event of a leak
        super.onDestroy();
        try{
            if(myBluetoothSocket!=null){
                myBluetoothSocket.close();
            }
            if(myOutputStream!=null){
                myOutputStream.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void setMessage(String command) {
        byte[] buffer = command.getBytes();

        if (myOutputStream != null) {
            try {
                myOutputStream.write(buffer);
                myOutputStream.flush();
            } catch (IOException e) {
                showMessage("i can't sent a message");
                e.printStackTrace();
            }
        }

    }

    //using reflection API(take the method from Bluetooth device) on JAVA
    private void startConnection(BluetoothDevice device) {
        if (device != null) {
            try {
                Method method = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                myBluetoothSocket = (BluetoothSocket) method.invoke(device, 1);
                myBluetoothSocket.connect();

                myOutputStream = myBluetoothSocket.getOutputStream();

                showMessage("Connection Successful");
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error Connection");
            }

        }
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_control);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        leftImageButton = findViewById(R.id.imageButton9);
        rightImageButton = findViewById(R.id.imageButton10);
        upImageButton = findViewById(R.id.imageButton8);
        downImageButton = findViewById(R.id.imageButton11);
        leftImageButton.setOnTouchListener(touchListener);
        rightImageButton.setOnTouchListener(touchListener);
        upImageButton.setOnTouchListener(touchListener);
        downImageButton.setOnTouchListener(touchListener);


        if (myBluetoothAdapter == null) {
            Log.d(TAG, "onCreate: Your device does not support bluetooth module");
            finish();
        }
        myDeviceList = new DeviceList(this, R.layout.device_item, myDevices);

        //Turn on Bluetooth
        enableBluetooth();
    }

    private void enableBluetooth() {
        Log.d(TAG, "enableBluetooth()");
        if (!myBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableBluetooth : Trying turn on Bluetooth");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (!myBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "onActivityResult : again sending request for turning on Bluetooth module");
                enableBluetooth();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
                searchDevice();
                break;
            case R.id.item_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Turn on searching Bluetooth devices
    private void searchDevice() {
        Log.d(TAG, "searchDevice()");
        enableBluetooth();
        //must control Android version and give geo location(must to be ,the rule from google)
        checkPermissionLoc();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver, filter);

        if (!myBluetoothAdapter.isDiscovering()) {
            Log.d(TAG, "searchDevice :Start searching devices");
            myBluetoothAdapter.startDiscovery();

        }
        if (myBluetoothAdapter.isDiscovering()) {
            Log.d(TAG, "searchDevice :searching already running...restarting");
            myBluetoothAdapter.cancelDiscovery();
            myBluetoothAdapter.startDiscovery();

        }

    }

    private void checkPermissionLoc() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int check = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            check += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");

            if (check != 0) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1002);
            }
        }
    }

    private void showListDevices() {
        Log.d(TAG, "showListDevices()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Searched devices");
        View view = getLayoutInflater().inflate(R.layout.list_device, null);
        listDevices = view.findViewById(R.id.list_devices);
        listDevices.setAdapter(myDeviceList);
        listDevices.setOnItemClickListener(itemOnClickListener);
        builder.setView(view);
        builder.create();
        builder.show();
    }

    //Text about work Bluetooth module
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}