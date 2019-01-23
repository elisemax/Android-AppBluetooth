/*
 * ©Copyright, 2018 Maxim Eliseykin, All right reserved.
 */

package cz.cvut.fs.robduino3;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceList extends ArrayAdapter<BluetoothDevice> {
    private LayoutInflater myLayoutInflater;
    private int myResourseView;
    private ArrayList<BluetoothDevice> myDevices=new ArrayList<>();

    DeviceList(Context context, int resource, ArrayList<BluetoothDevice> Devices) {
        super(context, resource,Devices);
        myLayoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myResourseView=resource;
        myDevices=Devices;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        convertView = myLayoutInflater.inflate(myResourseView,null);

        BluetoothDevice device=myDevices.get(position);
        TextView tvName=convertView.findViewById(R.id.tvNameDevices);
        TextView tvAddress=convertView.findViewById(R.id.tvAddressDevice);

        tvName.setText(device.getName());
        tvAddress.setText(device.getAddress());

        return convertView;
    }
}