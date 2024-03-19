package com.marsu.armuseumproject.database

import androidx.annotation.StringRes
import com.marsu.armuseumproject.R

data class Department(
    @StringRes val stringResourceId: Int, val id: Int
)

class Datasource {
    fun loadDepartments(): List<Department> {
        return listOf(
            Department(R.string.dep1, 1),
            Department(R.string.dep3, 3),
            Department(R.string.dep4, 4),
            Department(R.string.dep5, 5),
            Department(R.string.dep6, 6),
            Department(R.string.dep7, 7),
            Department(R.string.dep8, 8),
            Department(R.string.dep9, 9),
            Department(R.string.dep10, 10),
            Department(R.string.dep11, 11),
            Department(R.string.dep12, 12),
            Department(R.string.dep13, 13),
            Department(R.string.dep14, 14),
            Department(R.string.dep15, 15),
            Department(R.string.dep16, 16),
            Department(R.string.dep17, 17),
            Department(R.string.dep18, 18),
            Department(R.string.dep19, 19),
            Department(R.string.dep21, 21)
        )
    }
}