package com.marsu.armuseumproject.ui.theme

import android.view.RoundedCorner
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = CutCornerShape(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
        topStart = ZeroCornerSize,
        topEnd = ZeroCornerSize
    ),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(
        bottomStart = CornerSize(20.dp),
        bottomEnd = CornerSize(20.dp),
        topStart = ZeroCornerSize,
        topEnd = ZeroCornerSize
    ),
    large = RoundedCornerShape(20.dp)
)