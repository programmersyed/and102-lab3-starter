package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.bestsellerlistapp.R
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject


// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
//private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

/*
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the NY Times API.
 */
class BestSellerBooksFragment : Fragment(), OnListFragmentInteractionListener {

    /*
     * Constructing the view
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_best_seller_books_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    /*
     * Updates the RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.hide()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val params = RequestParams()
        //params["api-key"] = API_KEY

// Using the client, perform the HTTP request
        client[
                "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed",
                params,
                object : JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e("BestSellerBooksFragment", "It failed")
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                        val resultsJSON = json!!.jsonObject["results"].toString()
                        val gson = Gson()
                        val arrayBookType = object : TypeToken<List<BestSellerBook>>() {}.type
                        val models : List<BestSellerBook> = gson.fromJson(resultsJSON, arrayBookType)
                        Log.e("GENIOUS", resultsJSON.toString())
                        recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)
                    }
                }
        ]
        Log.e("eor", "System should be completed")
    }

    override fun onItemClick(item: BestSellerBook) {
        Log.e("Buton Pressed", "Successful")
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }
}

