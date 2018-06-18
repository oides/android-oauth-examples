package oidaslab.com.facebookoauth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.logging.Logger
import com.facebook.AccessToken



class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoginActivity::class.simpleName
    }

    val callbackManager: CallbackManager

    init {
        callbackManager = CallbackManager.Factory.create()
//        LoginManager.getInstance().logOut()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn) {
            Logger.getLogger(LoginActivity.TAG).warning("Usuário já logado...")
        } else {
            Logger.getLogger(LoginActivity.TAG).warning("Nenhum usuário logado...")
        }

        LoginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        Logger.getLogger(LoginActivity.TAG).warning("Login realizado com sucesso...")
                    }

                    override fun onCancel() {
                        Logger.getLogger(LoginActivity.TAG).warning("Login cancelado...")
                    }

                    override fun onError(error: FacebookException?) {
                        Logger.getLogger(LoginActivity.TAG).warning("Erro ao realizar login...")
                    }
                }
        );
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
