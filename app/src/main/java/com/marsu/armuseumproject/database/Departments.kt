package com.marsu.armuseumproject.database

import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import com.marsu.armuseumproject.R

data class Department(
    @StringRes val stringResourceId: Int,
    val id: Int
)

class Datasource() {
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
            Department(R.string.dep21, 21),
            /*Department(R.string.dep1, R.id.dep1),
            Department(R.string.dep3, R.id.dep3),
            Department(R.string.dep4, R.id.dep4),
            Department(R.string.dep5, R.id.dep5),
            Department(R.string.dep6, R.id.dep6),
            Department(R.string.dep7, R.id.dep7),
            Department(R.string.dep8, R.id.dep8),
            Department(R.string.dep9, R.id.dep9),
            Department(R.string.dep10, R.id.dep10),
            Department(R.string.dep11, R.id.dep11),
            Department(R.string.dep12, R.id.dep12),
            Department(R.string.dep13, R.id.dep13),
            Department(R.string.dep14, R.id.dep14),
            Department(R.string.dep15, R.id.dep15),
            Department(R.string.dep16, R.id.dep16),
            Department(R.string.dep17, R.id.dep17),
            Department(R.string.dep18, R.id.dep18),
            Department(R.string.dep19, R.id.dep19),
            Department(R.string.dep21, R.id.dep21),*/
        )
    }
}