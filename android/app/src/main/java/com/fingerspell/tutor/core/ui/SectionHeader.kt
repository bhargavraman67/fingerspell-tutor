package com.fingerspell.tutor.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.Teal

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            color = Teal,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Spacer(
            modifier = Modifier
                .padding(top = 0.dp)
                .width(44.dp)
                .height(3.dp)
                .background(Coral, RoundedCornerShape(2.dp))
        )
    }
}
