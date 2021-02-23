package com.kiran.student.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.kiran.student.R
import com.kiran.student.api.ServiceBuilder

class DashboardActivity : AppCompatActivity() {
    private lateinit var btnAddStudent : Button
    private lateinit var btnDisplayStudent : Button
    private lateinit var btnLocation : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        Toast.makeText(this, ServiceBuilder.token.toString(), Toast.LENGTH_SHORT).show()
        btnAddStudent = findViewById(R.id.btnAddStudent)
        btnDisplayStudent = findViewById(R.id.btnDisplayStudent)
       // btnLocation = findViewById(R.id.btnLocation)
        btnAddStudent.setOnClickListener{
            startActivity(Intent(this, AddStudentActivity::class.java))
        }
        btnDisplayStudent.setOnClickListener{
            startActivity(Intent(this, DisplayStudentActivity::class.java))
        }
//        btnLocation.setOnClickListener {
//
//        }
    }
}