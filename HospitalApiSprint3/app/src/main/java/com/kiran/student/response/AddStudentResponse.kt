package com.kiran.student.response

import com.kiran.student.entity.Student

data class AddStudentResponse (
    val success : Boolean? = null,
    val data : Student? = null
        )