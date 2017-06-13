package com.example.omi.locatemedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;


public class UserManualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView userManualTv = (TextView) findViewById(R.id.userManualTv);
        String userManualText = "This App lets you to send any Location in Google Map to anyone using SMS.The person who wants to send any Location, must select <font color=#303F9F>'Send Location Via SMS'</font> option. Then, he/she will be shown Google Maps and then he/she can select any Location by pressing and holding any position over Google Map. A send option will be shown afterwards which should be clicked by him/her. A message will be send to whom he/she wanted to send the Location.The other person receiving the SMS must have this App installed in his/her cell phone. He/she must select <font color=#303F9F>'Get Location From SMS'</font> option. Then, he/she must select the corresponding SMS to be decoded to Location in Google Map. Afterwards, the corresponding Location will be shown. ";
        userManualTv.setText(Html.fromHtml(userManualText));
    }
}
