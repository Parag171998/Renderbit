package com.example.renderbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.renderbit.Adapter.OrdersAdapter;
import com.example.renderbit.Models.Order;
import com.example.renderbit.Network.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    RecyclerView recyclerView;
    List<Order> orderList;
    Button navBTn;
    OrdersAdapter ordersAdapter;
    private final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBTn = findViewById(R.id.navBtn);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestLocationPermission();

        final Call<List<Order>> ordersCall = ApiClient.getInstance().getApi().getOrders();

        ordersCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                orderList = response.body();
                ordersAdapter = new OrdersAdapter(orderList,MainActivity.this);
                recyclerView.setAdapter(ordersAdapter);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });

        navBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderList != null) {
                    Collections.sort(orderList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            Order p1 = (Order) o1;
                            Order p2 = (Order) o2;
                            Date date1 = null;
                            Date date2 = null;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            try {
                                date1 = sdf.parse(p1.getCreatedAt());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                date2 = sdf.parse(p1.getCreatedAt());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (date1.after(date2)) {
                                return 1;
                            } else
                                return -1;
                        }
                    });
                }
                if(ordersAdapter != null)
                {
                    ordersAdapter.notifyDataSetChanged();
                }

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {

            if(isLocationEnabled(MainActivity.this))
                Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isLocationEnabled(MainActivity.this))
                  finish();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    public static Boolean isLocationEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Permission  Denied", Toast.LENGTH_SHORT).show();

        finish();
    }
}
