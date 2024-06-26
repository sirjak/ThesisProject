package com.marsu.armuseumproject.ui_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.window.Dialog
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Datasource
import com.marsu.armuseumproject.database.Department
import com.marsu.armuseumproject.database.PreferencesManager

@Composable
fun SelectDepartmentPopup(onDismiss: () -> Unit) {
    val preferencesManager = remember { PreferencesManager(MyApp.appContext) }

    val options = Datasource().loadDepartments()
    var optionInPrefs by remember { mutableStateOf<Department?>(null) }
    var (selectedOption, onOptionSelected) = remember { mutableStateOf<Department?>(null) }

    // Handle auto-selection
    if (selectedOption == null) {
        val id = preferencesManager.getData("selectedDepartment", null)
        val stringId = preferencesManager.getData("selectedDepartmentName", null)
        if (id !== null && id !== "" && stringId !== null && stringId !== "") {
            val department = Department(stringId.toInt(), id.toInt())
            optionInPrefs = department
            selectedOption = department
        }
    }

    fun addToSharedPrefs(department: Department) {
        preferencesManager.saveData("selectedDepartment", department.id.toString())
        preferencesManager.saveData(
            "selectedDepartmentName", department.stringResourceId.toString()
        )
    }

    Dialog(content = {
        Card(
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContentColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = MaterialTheme.colorScheme.error
            ), modifier = Modifier.fillMaxHeight(0.80f), shape = MaterialTheme.shapes.large
        ) {
            LazyColumn(
                modifier = Modifier
                    .selectableGroup()
                    .weight(0.94f)
            ) {
                itemsIndexed(options) { _, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (option == selectedOption),
                                onClick = { onOptionSelected(option) },
                                role = Role.RadioButton
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (option == selectedOption),
                            onClick = { onOptionSelected(option) })
                        Text(text = stringResource(id = option.stringResourceId))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.06f),
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    onClick = { onOptionSelected(null) },
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(text = stringResource(id = R.string.reset))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        if (selectedOption !== null && optionInPrefs !== selectedOption) {
                            addToSharedPrefs(selectedOption)
                        }
                        onDismiss()
                    }, shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(text = stringResource(id = R.string.back))
                }
            }
        }
    }, onDismissRequest = {
        if (selectedOption !== null && optionInPrefs !== selectedOption) {
            addToSharedPrefs(selectedOption)
        }
        onDismiss()
    })
}