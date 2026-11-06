package com.fingerspell.tutor.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.PaperWhite

@Composable
fun TutorChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedLabel = stringResource(R.string.state_selected)
    val plainLabel = stringResource(R.string.state_not_selected)
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .height(48.dp)
            .background(
                if (selected) Hairline else PaperWhite,
                RoundedCornerShape(14.dp)
            )
            .border(
                BorderStroke(1.dp, if (selected) Coral else Hairline),
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
            .semantics { stateDescription = if (selected) selectedLabel else plainLabel },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Coral else InkPrimary,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun ChipRow(
    items: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(items) { index, label ->
            TutorChip(
                label = label,
                selected = index == selectedIndex,
                onClick = { onSelect(index) }
            )
        }
    }
}
