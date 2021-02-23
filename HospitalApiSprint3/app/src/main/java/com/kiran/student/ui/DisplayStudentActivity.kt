package com.kiran.student.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiran.student.R
import com.kiran.student.adapters.StudentAdapter
import com.kiran.student.entity.Student
import com.kiran.student.userRepository.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayStudentActivity : AppCompatActivity() {
    lateinit var rvDisplayStudents : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_student)
        rvDisplayStudents = findViewById(R.id.rvDisplayStudents)
        CoroutineScope(Dispatchers.IO).launch {
            val repository = StudentRepository()
            val response = repository.getStudents()
            val lst = response.data
            withContext(Main){
                val adapter = StudentAdapter(lst as ArrayList<Student>,this@DisplayStudentActivity)
                rvDisplayStudents.adapter=adapter
                rvDisplayStudents.layoutManager = LinearLayoutManager(this@DisplayStudentActivity)
            }
        }

    }


}