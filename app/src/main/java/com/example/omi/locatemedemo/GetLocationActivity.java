package com.example.omi.locatemedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.omi.locatemedemo.adapter.SMSShowAdapter;
import com.example.omi.locatemedemo.model.SmsMessage;
import com.example.omi.locatemedemo.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class GetLocationActivity extends AppCompatActivity {

    public static final int READ_SMS_PERMISSION_CODE = 1;
    RecyclerView recyclerView;
    List<SmsMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.recyclerView = (RecyclerView) findViewById(R.id.smsRecyclerView);


        int readSMSPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if(readSMSPermissionResult == PackageManager.PERMISSION_GRANTED)
        {
            this.messages = this.getSMS();
            populateDataIntoRecyclerView();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},READ_SMS_PERMISSION_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == READ_SMS_PERMISSION_CODE)
        {

            //If permission is granted
            if(grantResults.length >0)
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    this.messages = this.getSMS();
                    populateDataIntoRecyclerView();

                }
                else
                {
                    Toast.makeText(this,"Oops you just denied the permissions",Toast.LENGTH_LONG).show();
                }


            }
            else
            {

                Toast.makeText(this,"Oops you just denied the permissions",Toast.LENGTH_LONG).show();
            }

        }
    }


    public List<SmsMessage> getSMS(){
        List<SmsMessage> sms = new ArrayList<>();
        Uri uriSMSURI = Uri.parse("content://sms/");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur.moveToNext())
        {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            SmsMessage smsMessage = new SmsMessage();
            smsMessage.setNumber(address);
            smsMessage.setBody(body);
            sms.add(smsMessage);

        }
        return sms;

    }


    private void populateDataIntoRecyclerView()
    {
        SMSShowAdapter showAdapter = new SMSShowAdapter(this,this.messages);
        this.recyclerView.setAdapter(showAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }


}
