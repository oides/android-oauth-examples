package oidaslab.com.facebookoauth.util

import android.graphics.Bitmap

object LoggedUser {

    lateinit var name: String
    lateinit var email: String
    lateinit var picture: Bitmap

    var userDataLoaded: Boolean = false
}


