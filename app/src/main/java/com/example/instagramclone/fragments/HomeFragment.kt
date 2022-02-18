package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.MainActivity
import com.example.instagramclone.Post
import com.example.instagramclone.PostAdapter
import com.example.instagramclone.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class HomeFragment : Fragment() {

    lateinit var postRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts: MutableList<Post> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set up views and click listeners
        postRecyclerView = view.findViewById(R.id.postRecyclerView)
        //Step to populate RecycleView
        //1. Create layout for each row in list
        //2. Create data source for each row (this is the Post class)
        //3. Create adapter that will bridge data and row layout
        //4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postRecyclerView.adapter = adapter
        //5. Set layout manager on RecycleView
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        queryPosts()
    }
    //Query for all posts in our server
    open fun queryPosts(){

        //Only return the most recent 20 posts
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Also give us back a user
        query.include(Post.KEY_USER)
        //Take in obj call back and tell us what to do when the post request is completed
        //Find all post objects
        //Return post in descending order: Newest post will appear
        query.addDescendingOrder("createdAt")
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
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    companion object{
        const val TAG = "HomeFragment"
    }
}