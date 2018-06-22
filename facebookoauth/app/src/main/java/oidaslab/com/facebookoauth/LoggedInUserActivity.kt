package oidaslab.com.facebookoauth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import kotlinx.android.synthetic.main.logged_in_user_activity.*
import oidaslab.com.facebookoauth.util.LoggedUser

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

        nameLoggedUser.text = LoggedUser.name
        emailLoggedUser.text = LoggedUser.email
    }

    public override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }

}
