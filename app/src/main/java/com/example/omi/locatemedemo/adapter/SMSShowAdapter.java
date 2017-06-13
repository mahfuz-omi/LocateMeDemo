package com.example.omi.locatemedemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omi.locatemedemo.R;
import com.example.omi.locatemedemo.ShowLocationActivity;
import com.example.omi.locatemedemo.model.SmsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omi on 6/12/2017.
 */

public class SMSShowAdapter extends RecyclerView.Adapter<SMSShowAdapter.ReportItemViewHolder> {

    Context context;
    List<SmsMessage> messages;
    LayoutInflater layoutInflater;

    public SMSShowAdapter(Context context, List<SmsMessage> messages) {
        super();
        this.context = context;
        this.messages = messages;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ReportItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.sms_row, parent, false);
        return new ReportItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportItemViewHolder holder, final int position) {

        SmsMessage smsMessage = this.messages.get(position);
        holder.number.setText(smsMessage.getNumber());
        holder.body.setText(smsMessage.getBody());
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public class ReportItemViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView number, body;

        public ReportItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.number = (TextView) itemView.findViewById(R.id.number);
            this.body = (TextView) itemView.findViewById(R.id.body);

            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    SmsMessage smsMessage = messages.get(position);
                    Intent intent = new Intent(context, ShowLocationActivity.class);
                    intent.putExtra("smsMessage",smsMessage);
                    //((Activity) context).finish();
                    context.startActivity(intent);
                }
            });
        }
    }

}

