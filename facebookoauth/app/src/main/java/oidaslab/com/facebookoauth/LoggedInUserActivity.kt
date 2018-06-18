package oidaslab.com.facebookoauth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker


class LoggedInUserActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoggedInUserActivity::class.simpleName
    }

    val accessTokenTracker: AccessTokenTracker

    init {
        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                    oldAccessToken: AccessToken?,
                    currentAccessToken: AccessToken?) {
                if (oldAccessToken != null && currentAccessToken == null) {
                    Log.d(LoggedInUserActivity.TAG, "Logout realizado com sucesso...")
                    val intent = Intent(this@LoggedInUserActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logged_in_user_activity)

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn) {
            Log.d(LoggedInUserActivity.TAG, "Usuário já logado...")
        } else {
            Log.d(LoggedInUserActivity.TAG, "Nenhum usuário logado...")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }

}
