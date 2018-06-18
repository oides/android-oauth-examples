package oidaslab.com.facebookoauth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoginActivity::class.simpleName
    }

    val callbackManager: CallbackManager

    init {
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        LoginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        Log.d(LoginActivity.TAG, "Login realizado com sucesso...")
                        val intent = Intent(this@LoginActivity, LoggedInUserActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onCancel() {
                        Log.d(LoginActivity.TAG, "Login cancelado...")
                    }

                    override fun onError(error: FacebookException?) {
                        Log.d(LoginActivity.TAG, "Erro ao realizar login...")
                    }
                }
        );
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
