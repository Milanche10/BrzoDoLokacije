package com.example.frontend

import com.example.frontend.models.Follow
import com.example.frontend.models.SocialPost
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.LinkedHashMap
import java.util.Objects


interface ApiInterface {

    @POST("auth/register")
    fun addUser(@Body newUser: UserDataItem): Call<UserDataItem>

    @POST("auth/login")
    fun loginUser(@Body newUser: UserDataItem): Call<UserDataItem>

    @GET("appuser/info/{email}")
    fun getUserData(@Path("email") email: String):Call<UserInfoItem>

    @GET("socialpost/all")
    fun getAllPost(): Call<List<PostDataItem>>

    @POST("socialpost/userposts")
    fun getUserPosts(@Body newUser: UserDataItem): Call<List<PostDataItemLocation>>

    @GET("appuser/info")
    fun getUserInfo(): Call<UserDataItem1>
    @POST("location")
    fun addLocation(@Body newLocation : Location): Call<Location>

    @POST("socialpost")
    fun addSocialPost(@Body newLocation : SocialPost): Call<SocialPost>

    @Multipart
    @POST("image/upload/{socialPostId}")
    fun uploadFile(@PartMap partMap: LinkedHashMap<String, RequestBody>, @Path("socialPostId") socialPostId: Int?): Call<Image?>?

    @GET("location/all")
    fun getAllLocations(): Call<List<LocationMap>>

    @Multipart
    @POST("image/uploadprofile/{userId}")
    fun uploadProfileImage(@PartMap partMap: LinkedHashMap<String, RequestBody>,@Path("userId") userId: Long?): Call<Image?>?

    @GET("socialpost/post/{postId}")
    fun getPostInfo(@Path("postId") postId: Long?):Call<PostInfoItem>
    
    @POST("socialpost/location")
    fun getAllPostsByLocation(@Body posts:PostDataItemLocation): Call<List<PostDataItemLocation>>
    
    @GET("socialpost/allsorted")
    fun getAllPostSortedByDate(): Call<List<PostDataItem>>

    @GET("socialpost/toprated")
    fun getAllPostSortedByRate(): Call<List<PostDataItem>>

    @POST("comment")
    fun addComment(@Body comment:CommentDataItem): Call<CommentDataItem>
    
    @PATCH("appuser")
    fun updateUserData(@Body newUser: UserDataItem):Call<UserInfoItem>

    @POST("like")
    fun addLike(@Body like:LikesDataItem): Call<LikesDataItem>

    @DELETE("like/{likeReactionId}")
    fun deleteLike(@Path("likeReactionId") postId: Long?): Call<Unit>

    @POST("like/post")
    fun getLikesForPost(@Body post:PostInfoItem): Call<List<LikesDataItem>>

    @POST("comment/post")
    fun getAllCommentsOnPost(@Body postId:PostCommentInfo?): Call<List<CommentDataItem>>

    @POST("comment/count")
    fun getCommentNumber(@Body postId:PostCommentInfo?): Call<Long>

    @DELETE("comment/delete/{commentId}")
    fun deleteComment(@Path("commentId") commentId: Long?): Call<Unit>

    @POST("appuser/follow/{userId}/{followedUserId}")
    fun followUser(@Path("userId") userId: Long?, @Path("followedUserId") followedUserId: Long?):Call<Follow>

    @DELETE("appuser/unfollow/{from}/{to}")
    fun unfollowUser(@Path("from") from: Long?, @Path("to") to: Long?):Call<UserInfoItem>

    @POST("appuser/countfollowers")
    fun getFollowers(@Body newUser: UserDataItem):Call<Int>

    @POST("appuser/countfollowing")
    fun getFollowingUsers(@Body newUser: UserDataItem):Call<Int>

    @GET("appuser/allfollows")
    fun getAllFollows():Call<List<Follow>>

    @POST("appuser/stat")
    fun getStat(@Body newUser: UserDataItem):Call<Statistic>

    @DELETE("location/{locationId}")
    fun deleteLocation(@Path("locationId") locationId: Long?): Call<String>

    @PATCH("appuser/updatepassword")
    fun updateUserPassword(@Body newUser: UserDataItem):Call<UserInfoItem>
    
    @POST("like/user")
    fun getAllLikedPostsByUser(@Body newUser: UserDataItem): Call<LikesPageDataItem>

    @GET("socialpost/date/{postId}")
    fun getDatePost(@Path("postId") postId: Long?):Call<String>
}