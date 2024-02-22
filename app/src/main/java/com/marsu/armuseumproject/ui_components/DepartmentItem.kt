package com.marsu.armuseumproject.ui_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.marsu.armuseumproject.database.Department

@Composable
fun DepartmentItem(department: Department) {
    val departmentText = stringResource(id = department.stringResourceId)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = false,
                onClick = {},
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = false, onClick = { /*TODO*/ })
        Text(text = departmentText)
    }
}