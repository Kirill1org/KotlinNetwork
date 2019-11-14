package bonch.dev.kotlinnetwork.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.kotlinnetwork.R
import bonch.dev.kotlinnetwork.adapers.AlbumsAdapter
import bonch.dev.kotlinnetwork.models.Album
import bonch.dev.kotlinnetwork.models.PrefTypes
import bonch.dev.kotlinnetwork.networking.InternetCheck
import bonch.dev.networking.networking.RetrofitFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AlbumsActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var albumsAdapter: AlbumsAdapter
    private lateinit var pref: SharedPreferences

    private lateinit var albumList: ArrayList<Album>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        initViews()

        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if (internet!!) {
                    getAlbums()
                } else {
                    loadAlbumsList()
                }
            }
        })

    }

    private fun loadAlbumsList() {
        pref = getSharedPreferences(PrefTypes.ALBUMS_PREFERENCES, MODE_PRIVATE);
        val gson = Gson()
        val type = object : TypeToken<List<Album>>() {}.type
        val json = pref.getString(PrefTypes.ALBUMS_STRING_TAG, "")

        if (!json.equals("")) {
            albumList = gson.fromJson(json, type);
            initRecyclerView(albumList)
        } else {
            Toast.makeText(this, "You should have an internet connection!", Toast.LENGTH_LONG)
                .show()
        }
    }


    private fun deleteAlbum(albumId: Int, albumPosition: Int) {


        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.deleteAlbum(albumId)

            try {
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        albumsAdapter.removeAlbum(albumPosition)

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "${response.errorBody()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (err: HttpException) {

                Log.e("Retrofit", "${err.printStackTrace()}")

            }
        }


    }

    private fun getAlbums() {

        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getAlbums(1)

            try {

                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        albumList = ArrayList<Album>()
                        albumList.addAll(response.body()!!)
                        saveAlbumsList(albumList)
                        initRecyclerView(albumList)

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "${response.errorBody()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (err: HttpException) {

                Log.e("Retrofit", "${err.printStackTrace()}")

            }
        }
    }

    private fun saveAlbumsList(almubList: ArrayList<Album>) {

        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(almubList)
        editor.putString(PrefTypes.ALBUMS_STRING_TAG, json)
        editor.commit()

    }

    private fun initViews() {
        rv = findViewById(R.id.albums_recycler_view)

        pref = getSharedPreferences(PrefTypes.ALBUMS_PREFERENCES, MODE_PRIVATE)

    }

    private fun initRecyclerView(list: ArrayList<Album>) {

        rv.layoutManager = LinearLayoutManager(this)
        albumsAdapter = AlbumsAdapter(list, this)
        rv.adapter = albumsAdapter
        albumsAdapter.onItemClick = { position ->

            deleteAlbum(albumsAdapter.list[position].id, position)

        }
    }
}
