package com.kiran.student.userRepository

import com.kiran.student.api.MyApiRequest
import com.kiran.student.api.ServiceBuilder
import com.kiran.student.api.StudentApi
import com.kiran.student.entity.Student
import com.kiran.student.response.*
import okhttp3.MultipartBody

class StudentRepository : MyApiRequest() {
    private val studentApi = ServiceBuilder.buildService(StudentApi::class.java)
    suspend fun addStudent(student:Student):AddStudentResponse{
        return  apiRequest {
            studentApi.addStudent(ServiceBuilder.token!!,student)
        }
    }
    suspend fun getStudents(): StudentResponse {
        return apiRequest {
            studentApi.getAllStudents(ServiceBuilder.token!!)
        }
    }
    suspend fun deleteStudents(student: Student): DeleteStudentResponse {
        return apiRequest {
            studentApi.deleteStudent(ServiceBuilder.token!!, student._id!!)
        }
    }
    suspend fun uploadImage(id: String, body: MultipartBody.Part)
            : ImageResponse {
        return apiRequest {
            studentApi.uploadImage(ServiceBuilder.token!!, id, body)
        }
    }

}