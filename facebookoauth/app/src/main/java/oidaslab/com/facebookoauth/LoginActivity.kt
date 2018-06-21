package oidaslab.com.facebookoauth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoginActivity::class.simpleName
    }

    lateinit var callbackManager: CallbackManager

    lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        callbackManager = CallbackManager.Factory.create()

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        LoginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        Log.d(LoginActivity.TAG, "Login realizado com sucesso...")
                        setFacebookData(result)

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

    private fun setFacebookData(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
                loginResult.accessToken,
                GraphRequest.GraphJSONObjectCallback { jsonObject, response ->
                    Log.d(LoginActivity.TAG, "Response: $response.toString()")

//                        val email = response.jsonObject.getString("email")
//                        val firstName = response.jsonObject.getString("first_name")
//                        val lastName = response.jsonObject.getString("last_name")
//                        val gender = response.jsonObject.getString("gender")
//
//
//                        val profile = Profile.getCurrentProfile()
//                        val id = profile.getId()
//                        val link = profile.getLinkUri().toString()
//                        Log.d(LoginActivity.TAG,"Link: $link")
//                        if (Profile.getCurrentProfile() != null) {
//                            Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200))
//                        }
//
//                        Log.d(LoginActivity.TAG,"Email: $email")
//                        Log.d(LoginActivity.TAG,"FirstName: $firstName")
//                        Log.d(LoginActivity.TAG,"LastName: $lastName")
//                        Log.d(LoginActivity.TAG,"Gender: $gender")


                }
        )

        val parameters = Bundle()
        parameters.putString("fields", "id,cover,email,first_name,last_name,gender")
        request.parameters = parameters
        request.executeAsync()
    }
}
