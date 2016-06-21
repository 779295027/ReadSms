package demo.materialdesign.sss.com.readsms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private SmsObserver smsObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsObserver = new SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);
    }


    private Uri SMS_INBOX = Uri.parse("content://sms/");

    public String getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
        String where = " date >  " + (System.currentTimeMillis() - 1000); //时间在一秒内的短信
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return "";
        if (cur.moveToLast()) {
//            String number = cur.getString(cur.getColumnIndex("address"));//手机号
//            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            Log.e("sms", body + "");
            Log.e("cur位置", cur.getPosition() + "");
            showToast(body);
            //这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String res = matcher.group().substring(1, 11);
//                mobileText.setText(res);
                showToast(res);
            }

            return body + "";
        }
        return "";
    }

    public Handler smsHandler = new Handler() {
        //这里可以进行回调的操作
        //TODO

    };

    class SmsObserver extends ContentObserver {
        private String s = "kong";


        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //每当有新短信到来时，使用我们获取短消息的方法
            s = getSmsFromPhone();
        }


    }

    private void showToast(String res) {
        Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_SHORT).show();
    }


}
