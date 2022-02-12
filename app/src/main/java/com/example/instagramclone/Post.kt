package com.example.instagramclone

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

/* To reference the post in Parse is importing ParseClassName and @ParseClassName*/
//Description: String
//Image:File
//User: User
@ParseClassName("Post")
/*Tell that the class is associated with ParseObj*/
class Post: ParseObject() {
    /*We need setter and getter for all the variables*/
    fun getDescription(): String?{
        return getString(KEY_DESCRIPTION)
    }
    fun setDescription(description: String){
        /*Put is a special method from the parse object that let us put specific key to specific value*/
        put(KEY_DESCRIPTION, description)
    }
    fun getImage(): ParseFile? {
        return getParseFile(KEY_IMAGE)
    }
    fun setImage (parseFile: ParseFile)
    {
        put(KEY_IMAGE,parseFile)
    }

    fun getUser(): ParseUser?
    {
        return getParseUser(KEY_USER)
    }
    fun setUser(user: ParseUser)
    {
        put (KEY_USER, user)
    }
    /*We need these key that match the names of the column in back4plan*/
    companion object{
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }
}