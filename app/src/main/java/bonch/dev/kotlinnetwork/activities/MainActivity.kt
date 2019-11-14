package bonch.dev.kotlinnetwork.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import bonch.dev.kotlinnetwork.R
import bonch.dev.kotlinnetwork.fragments.OnDialogButtonClick
import bonch.dev.kotlinnetwork.fragments.PostCreateDialogFragment
import bonch.dev.networking.networking.RetrofitFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainActivity : AppCompatActivity(), OnDialogButtonClick {

    private lateinit var albumsActivityBtn: Button
    private lateinit var photosActivityBtn: Button
    private lateinit var usersActivityBtn: Button
    private lateinit var postCreatedBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setClickListeners()
    }

    override fun onDialogClickListener(postTitle: String, postBody: String) {
        newPostCreated(postTitle,postBody)
    }

    private fun setClickListeners() {
        albumsActivityBtn.setOnClickListener {
            val intent = Intent(AlbumsActivity@ this, AlbumsActivity::class.java)
            startActivity(intent)
        }
        photosActivityBtn.setOnClickListener {
            val intent = Intent(PhotosActivity@ this, PhotosActivity::class.java)
            startActivity(intent)
        }
        usersActivityBtn.setOnClickListener {
            val intent = Intent(UsersActivity@ this, UsersActivity::class.java)
            startActivity(intent)
        }
        postCreatedBtn.setOnClickListener {
            var postCreatedDialogFragment: PostCreateDialogFragment = PostCreateDialogFragment()
            postCreatedDialogFragment.show(
                MainActivity@ this.supportFragmentManager,
                "postCreatedDialogFragment"
            )
        }

    }

    private fun initViews() {
        albumsActivityBtn = findViewById(R.id.btn_albums_activity)
        photosActivityBtn = findViewById(R.id.btn_photos_activity)
        usersActivityBtn = findViewById(R.id.btn_users_activity)
        postCreatedBtn = findViewById(R.id.btn_dialog_post_created)
    }

    private fun newPostCreated(postTitle: String, postBody: String) {

        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.createPost(postTitle, postBody)

            try {
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG).show()
                        Log.e("PostCreated",response.toString())
                    } else {
                        Toast.makeText(applicationContext, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                        Log.e("PostNotCreated",response.errorBody().toString())
                    }
                }

            } catch (err: HttpException) {
                Log.e("Retrofit", "${err.printStackTrace()}")
            }
        }


    }


}
