package com.example.instagramclone.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.instagramclone.MainActivity
import com.example.instagramclone.Post
import com.example.instagramclone.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File


class ComposeFragment : Fragment()
{
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var ivPreview: ImageView
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }
    //Submit Post BUtton
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
                Log.e(MainActivity.TAG, "Error While saving post")
                exception.printStackTrace()
                //TODO: Show a toast to tell user sth went wrong with saving the post
            }else{
                Log.i(MainActivity.TAG, "Successfully saved post")
                //TODO: Resetting the edit text field to be empty again
                //TODO: Reset the ImageView to empty
            }
        }
    }

    //Get Photo URL
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    //On Launch Camera code
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
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    //On Activity Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //Once we have the picture and take that file path and turn into bitmap
                //then display into imageView
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Set onClickListener and set up logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            ivPreview = view.findViewById(R.id.imageView)

            //Submit Button
        view.findViewById<Button>(R.id.btnSumit).setOnClickListener{
            //Send post to the server without an image
            //Grab the description that they input
            val description = view.findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                submitPost(description, user, photoFile!!)
            }else {
                //TODO PRINT error msg
                Log.i(MainActivity.TAG, "Error saved photo")
            }

        }

        //Take picture button
        view.findViewById<Button>(R.id.btnTakePicture).setOnClickListener{
            //Launch Camera to let user take picture
            onLaunchCamera()
        }


    }
}