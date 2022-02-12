package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.Parse
import com.parse.ParseUser

/*Where user can login*/
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Check if the user is logged in, if so take them to MainActivity
        //How: if a user is logged in, then it is not null; otherwise, null
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        //When the user click the login
        findViewById<Button>(R.id.login_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }
        //When the user click on sign up button
        findViewById<Button>(R.id.signupbtn).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }

    }

    private fun signUpUser(username: String, password: String)
    {
        // Create the ParseUser
        val user = ParseUser()
        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)
        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully created a new account
                //TODO: Navigate the user to the MainActivity
                goToMainActivity()
                Toast.makeText(this, "User Successfully signed in ", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "User failed to sign in ", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun loginUser(username: String, password: String){
            ParseUser.logInInBackground(username, password, ({ user, e ->
                if (user != null) {
                    Log.i(TAG, "Login Successfully ")
                    goToMainActivity()
                } else {
                    e.printStackTrace()
                    Toast.makeText(this,"Error logging in", Toast.LENGTH_SHORT).show()
                }})
            )
        }

    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
       finish() //Closing login activity and stay in main
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}