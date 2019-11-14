package bonch.dev.kotlinnetwork.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.kotlinnetwork.R
import bonch.dev.kotlinnetwork.adapers.PhotosAdapter

import bonch.dev.kotlinnetwork.models.Photo
import bonch.dev.kotlinnetwork.models.PhotoRealm
import bonch.dev.kotlinnetwork.networking.InternetCheck
import bonch.dev.networking.networking.RetrofitFactory
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import kotlin.collections.ArrayList


class PhotosActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var photosList: ArrayList<Photo>
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        initViews()


        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if (internet!!) {
                    getPhotos()
                } else {
                    loadPhotosList()
                }
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun loadPhotosList() {

        val loadedPhotosList = realm.where(PhotoRealm::class.java).findAll()
        if (!loadedPhotosList.isEmpty()) {
            val newPhotosList = ArrayList<Photo>()
            loadedPhotosList.forEach { photoRealm ->
                newPhotosList.add(
                    Photo(
                        photoRealm.id,
                        photoRealm.title.toString(),
                        photoRealm.url.toString()
                    )
                )
            }
            initRecyclerView(newPhotosList)
        } else Toast.makeText(
            this,
            "You should have an internet connection!",
            Toast.LENGTH_LONG
        ).show()


    }

    private fun getPhotos() {

        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getPhotos(1)

            try {
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        photosList = ArrayList()
                        photosList.addAll(response.body()!!)
                        savePhotosList(photosList)
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

    private fun savePhotosList(photosList: ArrayList<Photo>) {

        realm.executeTransaction { myRealms ->
            myRealms.where(PhotoRealm::class.java).findAll().deleteAllFromRealm()
        }
        realm.executeTransactionAsync({ myRealm ->
            photosList.forEach { photo ->
                val photoObject = myRealm.createObject(PhotoRealm::class.java)
                photoObject.id = photo.id
                photoObject.title = photo.title
                photoObject.url = photo.url

            }
        }, {
            val newPhotosList = ArrayList<Photo>()
            realm.where(PhotoRealm::class.java).findAll().forEach { photoRealm ->
                newPhotosList.add(
                    Photo(
                        photoRealm.id,
                        photoRealm.title.toString(),
                        photoRealm.url.toString()
                    )
                )
            }
            initRecyclerView(newPhotosList)

        },
            { throwable ->
                Log.e("Throwable message", throwable.message.toString())
            })

    }

    private fun initViews() {

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("new_photos.realm")
            .build()
        realm = Realm.getInstance(config)

        rv = findViewById(R.id.photos_recycler_view)

    }

    private fun initRecyclerView(list: ArrayList<Photo>) {

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = PhotosAdapter(list, this)

    }


}
