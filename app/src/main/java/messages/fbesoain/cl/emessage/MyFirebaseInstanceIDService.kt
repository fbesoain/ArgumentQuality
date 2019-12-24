package messages.fbesoain.cl.emessage

import android.content.Context
import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("TOKEN: ", refreshedToken)
        val sharedPreferences = baseContext.getSharedPreferences(
                "EvaluacionMensaje", Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putString("FirebaseToken", refreshedToken).apply()
    }

}