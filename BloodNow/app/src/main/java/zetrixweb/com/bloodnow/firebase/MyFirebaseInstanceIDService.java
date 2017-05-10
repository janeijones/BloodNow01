package zetrixweb.com.bloodnow.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


     //Called if InstanceID token is updated.

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        Toast.makeText(this, "token" + refreshedToken, Toast.LENGTH_SHORT).show();

        // To send msgs to app on server side, send the Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }


    /* use this method to associate the user's FCM InstanceID token with any server-side account
      maintained by the app. -- not completed */
    private void sendRegistrationToServer(String token) {
        // not completed: send token to app server     }
}
