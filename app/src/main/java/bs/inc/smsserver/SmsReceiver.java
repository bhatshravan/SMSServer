package bs.inc.smsserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shravan on 23/3/18.
 */
public class SmsReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String messageSender = smsMessage.getOriginatingAddress();
                Map messageMap = new HashMap();
                messageMap.put("message",messageBody);
                messageMap.put("sender",messageSender);
                messageMap.put("time", ServerValue.TIMESTAMP);

                DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference().child("feedback").child("sms");
                String push_id = mRoot.push().getKey().toString();
                mRoot.child(push_id).setValue(messageMap);
            }
        }
    }
}

    /*private String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        String str = "";

        if (bundle != null){
            // Retrieve the Binary SMS data
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received (although multipart is not supported with binary)
            for (int i=0; i<msgs.length; i++) {
                byte[] data = null;

                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                str += "Binary SMS from " + msgs[i].getOriginatingAddress() + " :";

                str += "\nBINARY MESSAGE: ";

                // Return the User Data section minus the
                // User Data Header (UDH) (if there is any UDH at all)
                data = msgs[i].getUserData();

                // Generally you can do away with this for loop
                // You'll just need the next for loop
                for (int index=0; index < data.length; index++) {
                    str += Byte.toString(data[index]);
                }

                str += "\nTEXT MESSAGE (FROM BINARY): ";

                for (int index=0; index < data.length; index++) {
                    str += Character.toString((char) data[index]);
                }

                str += "\n";
            }

            // Dump the entire message
            // Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            Log.d(TAG, str);
        }
    }
}*/