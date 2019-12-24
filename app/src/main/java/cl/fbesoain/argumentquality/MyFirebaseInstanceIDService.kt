package cl.fbesoain.argumentquality

import android.content.Context
import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MyFirebaseInstanceIDService : FirebaseMessagingService() {


    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("NEW_TOKEN", s)
    }

    /*
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("TOKEN: ", refreshedToken)
        val sharedPreferences = baseContext.getSharedPreferences(
                "EvaluacionMensaje", Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putString("FirebaseToken", refreshedToken).apply()
    }
*/
}