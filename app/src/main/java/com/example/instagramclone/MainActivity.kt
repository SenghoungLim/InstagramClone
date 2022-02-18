package com.example.instagramclone

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.instagramclone.fragments.ComposeFragment
import com.example.instagramclone.fragments.HomeFragment
import com.example.instagramclone.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File
import java.util.*

/*
    1. Let User create a post by taking a photo with their camera
            -> we need a login so we know which user's data
 */
class MainActivity : AppCompatActivity() {

    //Tell android what code we use to launch activity to start a camera app
    //Name of the photo when the photo was taken
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //1. Setting the description of the post
        //2. A button to launch the camera to take a picture
        //3. An imageView to show the picture the user has taken
        //4. A button to save and send the post to our Parse Server

        //A manager for fragment swapping
        val fragmentManager: FragmentManager = supportFragmentManager
        //A var on which item is selected, it will set that fragment into this Var
        var fragmentToShow: Fragment? = null
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener{
            item ->
            when(item.itemId){
                R.id.action_home -> {
                    fragmentToShow = HomeFragment()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
                R.id.action_compose-> {
                    fragmentToShow = ComposeFragment()
                }
            }
            if(fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow!!).commit()
            }
            //setOnItemSelectedListener signify if we handle the user interaction which tell the
            //program by returning a boolean value. "True" means yes
            true
        }
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home

        //queryPosts()
    }


    companion object{
        const val TAG = "MainActivity"
    }
}