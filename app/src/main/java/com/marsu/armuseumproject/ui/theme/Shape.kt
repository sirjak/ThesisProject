package com.marsu.armuseumproject.ui.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = CutCornerShape(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
        topStart = CornerSize(16.dp),
        topEnd = ZeroCornerSize
    ),
    large = RoundedCornerShape(0.dp)
)