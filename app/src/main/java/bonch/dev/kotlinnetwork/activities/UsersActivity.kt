package bonch.dev.kotlinnetwork.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.kotlinnetwork.R
import bonch.dev.kotlinnetwork.adapers.UsersAdapter
import bonch.dev.kotlinnetwork.models.PrefTypes
import bonch.dev.kotlinnetwork.models.User
import bonch.dev.kotlinnetwork.networking.InternetCheck
import bonch.dev.networking.networking.RetrofitFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UsersActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var usersList: ArrayList<User>
    private lateinit var pref: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        initViews()

        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if (internet!!) {
                    getUsers()
                } else {
                    loadUsersList()
                }
            }
        })

    }

    private fun loadUsersList() {
        pref = getSharedPreferences(PrefTypes.USERS_PREFERENCES, MODE_PRIVATE);
        val gson = Gson()
        val type = object : TypeToken<List<User>>() {}.type
        val editor = pref.edit()
        val json = pref.getString(PrefTypes.USERS_STRING_TAG, "")

        if (!json.equals("")) {
            usersList = gson.fromJson(json, type);
            initRecyclerView(usersList)

        } else {

            Toast.makeText(this, "You should have an internet connection!", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getUsers() {

        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getUsers()

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        usersList = ArrayList<User>()
                        usersList.addAll(response.body()!!)
                        saveUsersList(usersList)
                        initRecyclerView(usersList)
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

    private fun saveUsersList(usersList: java.util.ArrayList<User>) {

        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(usersList)
        editor.putString(PrefTypes.USERS_STRING_TAG, json)
        editor.commit()

    }

    private fun initViews() {
        rv = findViewById(R.id.users_recycler_view)
        pref = getSharedPreferences(PrefTypes.USERS_PREFERENCES, MODE_PRIVATE)
    }

    private fun initRecyclerView(list: ArrayList<User>) {
        rv.adapter = UsersAdapter(list, this)
        rv.layoutManager = LinearLayoutManager(this)
    }
}
