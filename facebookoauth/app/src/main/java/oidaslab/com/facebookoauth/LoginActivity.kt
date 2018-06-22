package oidaslab.com.facebookoauth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import oidaslab.com.facebookoauth.util.LoggedUser
import android.R.attr.data
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoginActivity::class.simpleName
    }

    lateinit var callbackManager: CallbackManager
    lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        checkLoggedInUser()
        configureLoginManager()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkLoggedInUser() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn) {
            Log.d(TAG, "Usuário já logado...")
            setFacebookData(accessToken)
        } else {
            Log.d(TAG, "Nenhum usuário logado...")
        }
    }

    private fun configureLoginManager() {

        LoginManager.getInstance()

        callbackManager = CallbackManager.Factory.create()

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        Log.d(TAG, "Login realizado com sucesso...")
                        setFacebookData(result.accessToken)
                    }

                    override fun onCancel() {
                        Log.d(TAG, "Login cancelado...")
                    }

                    override fun onError(error: FacebookException?) {
                        Log.d(TAG, "Erro ao realizar login...")
                    }
                }
        );
    }

    private fun setFacebookData(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
                accessToken,
                GraphRequest.GraphJSONObjectCallback { jsonObject, response ->
                    Log.d(LoginActivity.TAG, "Response: $response.toString()")

                    LoggedUser.name = response.jsonObject.getString("name")
                    LoggedUser.email = response.jsonObject.getString("email")

                    val profilePicUrl = URL(response.jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"))
                    doAsync {
                        LoggedUser.picture = BitmapFactory.decodeStream(profilePicUrl.openConnection().getInputStream())

                        val intent = Intent(this@LoginActivity, LoggedInUserActivity::class.java)
                        startActivity(intent)
                    }
                }
        )

        val parameters = Bundle()
        parameters.putString("fields", "id,cover,email,name,picture.height(350).width(350)")
        request.parameters = parameters
        request.executeAsync()
    }

}
