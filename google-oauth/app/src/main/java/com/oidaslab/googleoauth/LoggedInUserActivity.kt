package com.oidaslab.googleoauth

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.logged_in_user_activity.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


class LoggedInUserActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoggedInUserActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logged_in_user_activity)

        updateButtonLabel()

        configureLogout()
    }

    private fun configureLogout() {
        sign_in_button.setOnClickListener {
            Log.d(TAG, "Iniciando logout no google")

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }

        }
    }

    private fun updateButtonLabel() {
        val buttonLabel = sign_in_button.getChildAt(0) as TextView
        buttonLabel.text = "Logout"
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            Log.d(TAG, "Usuário logado")
            name.text = account.displayName
            email.text = account.email

            val profilePicUrl = URL(account.photoUrl.toString() + "?sz=750")
            doAsync {
                val image = BitmapFactory.decodeStream(profilePicUrl.openConnection().getInputStream())
                uiThread {
                    pictureLoggedUser.setImageBitmap(image)
                }
            }


        } else {
            Log.d(TAG, "Usuário não logado")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
