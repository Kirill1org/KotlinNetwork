package bonch.dev.networking.networking

import bonch.dev.kotlinnetwork.models.Album
import bonch.dev.kotlinnetwork.models.Photo
import bonch.dev.kotlinnetwork.models.User
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.*


interface RetrofitService {

    @GET("/albums")
    suspend fun getAlbums(@Query("userId") userId: Int) : Response<List<Album>>

    @GET("/photos")
    suspend fun getPhotos(@Query("albumId") albumId: Int) : Response<List<Photo>>

    @GET("/users")
    suspend fun getUsers() : Response<List<User>>

    @DELETE("/albums/{id}")
    suspend fun deleteAlbum(@Path("id") id: Int): Response<ResponseBody>

    @FormUrlEncoded
    @POST("/posts")
    suspend fun createPost(@Field("title") title:String,
                           @Field("body") body: String) : Response<ResponseBody>



}