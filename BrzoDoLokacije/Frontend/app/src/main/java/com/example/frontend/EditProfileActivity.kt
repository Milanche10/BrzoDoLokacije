package com.example.frontend

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList
import java.util.LinkedHashMap

class EditProfileActivity : AppCompatActivity() {

    private lateinit var circleIV: CircleImageView;
    private lateinit var button: CircleImageView;
    private lateinit var saveButton: Button;
    var k : Int = 0;
    var selectedPhotoUri: Uri? = null
    var validationText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        verifyStoragePermissions(this)

        button = findViewById(R.id.addPhoto)
        circleIV = findViewById(R.id.img_profile)
        saveButton = findViewById(R.id.btn_save_changes)

        var context: Context = applicationContext;
        val session = SessionManager(context)
        System.out.println(session.getEmail().toString())

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<UserInfoItem> = apiInterface.getUserData(session.getEmail().toString())

        requestCall.enqueue(object : Callback<UserInfoItem> {

            override fun onResponse(
                call: Call<UserInfoItem>,
                response: Response<UserInfoItem>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    findViewById<TextView>(R.id.firstnameEditText).text = responseBody?.firstName
                    findViewById<TextView>(R.id.lastnameEditText).text = responseBody?.lastName

                    if(responseBody?.image != null) {
                        var profilePhoto : Image = responseBody?.image as Image
                        Picasso.get().load(profilePhoto.url).resize(150, 150).centerCrop().into(circleIV)
                    }

                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

        validationText = findViewById(R.id.validationText)

        button.setOnClickListener {
            verifyStoragePermissions(this)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        saveButton.setOnClickListener{
            /*if(k == 1) {
                sendPhotoRequest(session.getId())
            }*/

            if(findViewById<TextView>(R.id.firstnameEditText).text.toString() != "" && findViewById<TextView>(R.id.lastnameEditText).text.toString() != "")
            {
                updateProfileRequest(findViewById<TextView>(R.id.firstnameEditText).text.toString(), findViewById<TextView>(R.id.lastnameEditText).text.toString())
                var intent = Intent(this@EditProfileActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            else {
                validationText!!.text = "Sva polja moraju biti popunjena."
            }
        }


    }
    private fun verifyStoragePermissions(activity: Activity) {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val REQUEST_EXTERNAL_STORAGE = 1
        // check if we have write permission
        val permission: Int =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var loadingDialog: LoadingDialog = LoadingDialog(this@EditProfileActivity)
        var context: Context = applicationContext;
        val session = SessionManager(context)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            var localImageUri:Uri? = data.data
            circleIV.setImageURI(data.data)
            if(localImageUri != null){
                selectedPhotoUri = localImageUri
                //k = 1
                sendPhotoRequest(session.getId())
                loadingDialog.startLoadingDialog()
                Handler().postDelayed({ loadingDialog.dismissDialog() }, 9000)
            }
        }
    }

    fun sendPhotoRequest(userID: Long?)
    {
        var otherImagesFiles = mutableListOf<File>()
        otherImagesFiles.add(File(getRealPathFromUri(selectedPhotoUri!!)!!))

        var requestBody: RequestBody
        val mapRequestBody = LinkedHashMap<String, RequestBody>()
        val arrBody: MutableList<MultipartBody.Part> = ArrayList()

        var body: MultipartBody.Part ? = null
        Log.d(ContentValues.TAG, "file===" + otherImagesFiles[0].getName());

        println("PRVAPRVAPRVAPRVA")
        println(otherImagesFiles[0].getName())

        requestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            otherImagesFiles[0]
        )
        mapRequestBody["file\"; filename=\"" + otherImagesFiles[0].getName()] = requestBody
        mapRequestBody["test"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "file")
        body = MultipartBody.Part.createFormData(
            "fileName",
            otherImagesFiles[0].getName(),
            requestBody
        )
        arrBody.add(body)

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<Image?>? = apiInterface.uploadProfileImage(mapRequestBody, userID)

        if (requestCall != null) {
            requestCall.enqueue(object : Callback<Image?> {

                override fun onResponse(
                    call: Call<Image?>,
                    response: Response<Image?>
                ) {
                    if (response.isSuccessful) {
                        println("Uspesan zahtev")
                    }
                }

                override fun onFailure(call: Call<Image?>, t: Throwable) {
                    println("Neuspesan zahtev")
                }
            })
        }
    }

    fun updateProfileRequest(firstname: String, lastname: String) {

            var context: Context = applicationContext;
            val session = SessionManager(context)

            var newUser = UserDataItem(
                session.getId(),
                firstname,
                lastname,
                session.getEmail(),
                null,
                session.getToken()
            );

            val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
            val requestCall: Call<UserInfoItem> = apiInterface.updateUserData(newUser)

            requestCall.enqueue(object : Callback<UserInfoItem> {

                override fun onResponse(
                    call: Call<UserInfoItem>,
                    response: Response<UserInfoItem>
                ) {
                    if (response.isSuccessful) {

                        val responseBody = response.body()
                        findViewById<TextView>(R.id.firstnameEditText).text = responseBody?.firstName
                        findViewById<TextView>(R.id.lastnameEditText).text = responseBody?.lastName

                        if (responseBody?.image != null) {
                            var profilePhoto: Image = responseBody?.image as Image
                            Picasso.get().load(profilePhoto.url).resize(150, 150).centerCrop()
                                .into(circleIV)
                        }

                        println("Uspesan zahtev")
                    }
                }

                override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                    println("Neuspesan zahtev")
                }
            })
    }

    fun getRealPathFromUri(contentUri: Uri) :String? {

        var result: String? = null

        val cursor = this.contentResolver?.query(contentUri, null, null, null, null)
        if(cursor == null){
            result = contentUri.path
        }
        else{
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }

        return result
    }

}