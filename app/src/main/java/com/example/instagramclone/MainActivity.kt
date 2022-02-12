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
import com.parse.*
import java.io.File
import java.util.*

/*
    1. Let User create a post by taking a photo with their camera
            -> we need a login so we know which user's data
 */
class MainActivity : AppCompatActivity() {

    //Tell android what code we use to launch activity to start a camera app
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    //Name of the photo when the photo was taken
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //1. Setting the description of the post
        //2. A button to launch the camera to take a picture
        //3. An imageView to show the picture the user has taken
        //4. A button to save and send the post to our Parse Server


        findViewById<Button>(R.id.btnSumit).setOnClickListener{
            //Send post to the server without an image
            //Grab the description that they input
            val description = findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                submitPost(description, user, photoFile!!)
            }else {
                //TODO PRINT error msg
                Log.i(TAG, "Error saved photo")
            }

        }

        findViewById<Button>(R.id.btnTakePicture).setOnClickListener{
            //Launch Camera to let user take picture
            onLaunchCamera()
        }

        queryPosts()
    }

    //Function for submitting the post
    fun submitPost(description: String, user: ParseUser, file: File) {
        //create the post object
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        /*We need to do it in the background so it does not interrupt with UI*/
        /*We need to return an exception if sth has gone wrong*/
        post.saveInBackground{exception ->
            if (exception != null) {
                Log.e(TAG, "Error While saving post")
                exception.printStackTrace()
                //TODO: Show a toast to tell user sth went wrong with saving the post
            }else{
                    Log.i(TAG, "Successfully saved post")
                //TODO: Resetting the edit text field to be empty again
                //TODO: Reset the ImageView to empty
            }
            }
    }
    /*This method is called when the users come from the camera app*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //Once we have the picture and take that file path and turn into bitmap
                //then display into imageView
                val ivPreview: ImageView = findViewById(R.id.imageView)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
   //Call to launch a camera through intent
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        //Access all the app that can take a pic, and let user choose it
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        //Where to save the picture, works differently on different version
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    //Query for all posts in our server
    fun queryPosts(){

        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Also give us back a user
        query.include(Post.KEY_USER)
        //Take in obj call back and tell us what to do when the post request is completed
        //Find all post objects
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?){
                if (e != null){
                    //sth wrong
                    Log.e(TAG, "Error Fetching Data")
                } else{
                    if (posts != null){
                        for (post in posts){
                            Log.i(TAG, "Post: " + post.getDescription() + " ,username: " +
                            post.getUser()?.username)
                        }
                    }
                }
            }
        })
    }
    companion object{
        const val TAG = "MainActivity"
    }
}