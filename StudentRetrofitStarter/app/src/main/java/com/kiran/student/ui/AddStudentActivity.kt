package com.kiran.student.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import com.kiran.student.R
import com.kiran.student.entity.Student
import com.kiran.student.userRepository.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddStudentActivity : AppCompatActivity() {
    lateinit var etFullName : EditText
    lateinit var etAddress : EditText
    lateinit var etAge : EditText
    lateinit var rbMale : RadioButton
    lateinit var rbFemale : RadioButton
    lateinit var rbOthers : RadioButton
    lateinit var btnAdd : Button
    lateinit var imgAddImage: ImageView
    var gender = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)
        etFullName = findViewById(R.id.etFullName)
        etAddress = findViewById(R.id.etAddress)
        etAge = findViewById(R.id.etAge)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        rbOthers = findViewById(R.id.rbOthers)
        imgAddImage = findViewById(R.id.imgAddImage)
        btnAdd = findViewById(R.id.btnAdd)


        btnAdd.setOnClickListener{
            when{
                rbMale.isChecked-> gender = rbMale.text.toString()
                rbFemale.isChecked-> gender = rbFemale.text.toString()
                rbOthers.isChecked-> gender = rbOthers.text.toString()
            }

            val student = Student(fullname = etFullName.text.toString(), address = etAddress.text.toString(), age = etAge.text.toString().toInt(),gender = gender)
            CoroutineScope(Dispatchers.IO).launch {
                val repository = StudentRepository()
                val response = repository.addStudent(student)
                if(response.success == true){
                    if(imageUrl != null){
                        uploadImage(response.data!!._id!!)
                    }

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@AddStudentActivity, "Success", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@AddStudentActivity, "Error adding student", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        imgAddImage.setOnClickListener{
            loadPopUpMenu()
        }
    }
    private fun uploadImage(studentId: String) {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val reqFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body =
                MultipartBody.Part.createFormData("file", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val studentRepository = StudentRepository()
                    val response = studentRepository.uploadImage(studentId, body)
                    if (response.success == true) {
                        withContext(Main) {
                            Toast.makeText(this@AddStudentActivity, "Uploaded", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Main) {
                        Log.d("Error ....!!! ", ex.localizedMessage)
                        Toast.makeText(
                            this@AddStudentActivity,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    // Load pop up menu
    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this@AddStudentActivity, imgAddImage)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    /*Toast.makeText(this@AddStudent,"Clicked",Toast.LENGTH_SHORT).show()*/
                     openCamera()
                R.id.menuGallery ->
                    /*Toast.makeText(this@AddStudent,"Clicked",Toast.LENGTH_SHORT).show()*/
                    openGallery()
            }
            true
        }
        popupMenu.show()
    }

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    private fun openGallery() {
        val galleryintent = Intent(Intent.ACTION_PICK)
        galleryintent.type ="image/*"
        startActivityForResult(galleryintent, REQUEST_GALLERY_CODE)
    }
    private fun openCamera() {
        val cameraintent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraintent, REQUEST_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                imgAddImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            }
            else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                imgAddImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }

        }

    }
    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

}