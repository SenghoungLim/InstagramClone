package com.example.instagramclone

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject

//Whenever the Android First created, we want to initialize parse
//With the application ID, Client Key and server
class CPathIGApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            ParseObject.registerSubclass(Post::class.java)
            Parse.initialize(
                Parse.Configuration.Builder(this)
                    .applicationId(getString(R.string.back4app_app_id))
                    .clientKey(getString(R.string.back4app_client_key))
                    .server(getString(R.string.back4app_server_url))
                    .build());
        }
    }
