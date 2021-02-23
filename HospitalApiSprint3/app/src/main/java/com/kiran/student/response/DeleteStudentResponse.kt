package com.kiran.student.response

import com.kiran.student.entity.Student

data class DeleteStudentResponse (
    val success : Boolean? = null,
    val data : Student? = null
        )