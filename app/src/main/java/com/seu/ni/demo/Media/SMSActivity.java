package com.seu.ni.demo.Media;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seu.ni.demo.R;

import java.util.ArrayList;

public class SMSActivity extends AppCompatActivity implements View.OnClickListener {
    public final String TAG = "-----SMS----->";
    public final String ACTION_SMS_REC = "android.provider.Telephony.SMS_RECEIVED";
    public final String ACTION_SMS_SENDED = "com.seu.ni.demo.SMS_SENDED";
    public final String SMS_URI_ALL = "content://sms/";
    public final String SMS_URI_INBOX = "content://sms/inbox";
    public final String SMS_URI_SENT = "content://sms/sent";
    public final String SMS_URI_DRAFT = "content://sms/draft";
    public final String SMS_URI_OUTBOX = "content://sms/outbox";
    public final String SMS_URI_FAILED = "content://sms/failed";
    public final String SMS_URI_QUEUED = "content://sms/queued";
    private TextView mReceTel, mReceContent;
    private EditText mSendTel, mSendContent;
    private Button mSend;
    private SmsManager mSmsManager;
    private SmsReceiver mSmsReceiver;
    private SendStatusReceiver mSendStatusReceiver;


    public <T> void L(T para) {
        Log.i(TAG, para + "");
    }

    public <T> void T(T para) {
        Toast.makeText(this, "" + para, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        initView();
        registerReceiver();
        readSms();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sms_send:
                String _Tel = mSendTel.getText().toString();
                String _Text = mSendContent.getText().toString();

                if (_Tel.isEmpty() || _Text.isEmpty()) {
                    T("Tel and Text can not be empty!");
                    return;
                } else {
                    sendSms(_Tel, _Text);
                }
                break;
            default:
                return;
        }
    }

    /**
     * Read Sms from phone
     */
    private void readSms() {
        ContentResolver _ContentResolver = getContentResolver();
        Cursor _Cursor = _ContentResolver.query(Uri.parse(SMS_URI_INBOX), null, null, null, null);
        while (_Cursor.moveToNext()) {
            int _TelColum = _Cursor.getColumnIndex("address");
            int _TextColum = _Cursor.getColumnIndex("body");
            L(_Cursor.getString(_TelColum) + _Cursor.getString(_TextColum));
        }

    }

    private void sendSms(String pTel, String pText) {

        mSmsManager = SmsManager.getDefault();
        Intent _Intent = new Intent(ACTION_SMS_SENDED);
        PendingIntent _SentIntent = PendingIntent.getBroadcast(this, 0, _Intent, 0);

        if (pText.length() > 70) {
            // SMS too long, needs to be divided
            // needs android.permission.READ_PHONE_STATE
            ArrayList<String> _TextList = mSmsManager.divideMessage(pText);
            ArrayList<PendingIntent> _SentIntentList = new ArrayList<>();
            for (int i = 0; i < _TextList.size(); i++) {
                _SentIntentList.add(_SentIntent);
            }
            mSmsManager.sendMultipartTextMessage(pTel, null, _TextList, _SentIntentList, null);
        } else {

            mSmsManager.sendTextMessage(pTel, null, pText, _SentIntent, null);
        }
        mSendContent.setText("");
        mSendTel.setText("");
    }


    private void initView() {
        mReceTel = (TextView) findViewById(R.id.tv_sms_tel);
        mReceContent = (TextView) findViewById(R.id.tv_sms_content);
        mSendTel = (EditText) findViewById(R.id.et_sms_tel);
        mSendContent = (EditText) findViewById(R.id.et_sms_content);
        mSend = (Button) findViewById(R.id.bt_sms_send);

        mSend.setOnClickListener(this);
    }

    private void registerReceiver() {
        //receive  a sms
        mSmsReceiver = new SmsReceiver();
        IntentFilter _SmsRecFilter = new IntentFilter(ACTION_SMS_REC);
        registerReceiver(mSmsReceiver, _SmsRecFilter);

        //receive msm send state
        mSendStatusReceiver = new SendStatusReceiver();
        IntentFilter _SmsStatusFilter = new IntentFilter(ACTION_SMS_SENDED);
        registerReceiver(mSendStatusReceiver, _SmsStatusFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmsReceiver);
        unregisterReceiver(mSendStatusReceiver);
    }

    private class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object pdus[] = (Object[]) bundle.get("pdus");
                SmsMessage messages[] = new SmsMessage[pdus.length];

                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                String address = messages[0].getOriginatingAddress();
                String fullMessage = "";
                for (SmsMessage message : messages) {
                    fullMessage += message.getMessageBody();
                }
                mReceTel.setText("From: " + address);
                mReceContent.setText("Msg: " + fullMessage);
            }

        }
    }

    private class SendStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (getResultCode() == RESULT_OK) {
                T("Delivered!");
            } else {
                T("Sorry, deliver failed!");
            }
        }
    }
}
