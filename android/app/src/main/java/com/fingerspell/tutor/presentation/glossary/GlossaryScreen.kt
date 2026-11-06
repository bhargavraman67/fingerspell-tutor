package com.fingerspell.tutor.presentation.glossary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.theme.ContentOverlay
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.PaperWhite
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.ChipRow
import com.fingerspell.tutor.core.ui.HandshapeCanvas
import com.fingerspell.tutor.core.ui.SignMotionView
import com.fingerspell.tutor.core.ui.TopBar

@Composable
fun GlossaryRoute(onBack: () -> Unit, onHome: () -> Unit, onOpenLetter: (String) -> Unit) {
    val glossaryViewModel: GlossaryViewModel = viewModel(factory = ServiceLocator.factory)
    val state by glossaryViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        glossaryViewModel.refresh()
    }
    GlossaryScreen(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onQuery = glossaryViewModel::setQuery,
        onFilter = glossaryViewModel::setFilter,
        onOpenLetter = onOpenLetter
    )
}

@Composable
fun GlossaryScreen(
    state: GlossaryUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onQuery: (String) -> Unit,
    onFilter: (Int) -> Unit,
    onOpenLetter: (String) -> Unit
) {
    val tone = skinTone(state.skinToneIndex)
    val filters = listOf(
        stringResource(R.string.filter_all),
        stringResource(R.string.filter_letters),
        stringResource(R.string.filter_signs),
        stringResource(R.string.filter_known)
    )
    AppBackdrop(overlay = ContentOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            TopBar(
                title = stringResource(R.string.glossary_title),
                onBack = onBack,
                onHome = onHome,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            OutlinedTextField(
                value = state.query,
                onValueChange = onQuery,
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(R.string.cd_search),
                        tint = InkSecondary
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.glossary_search),
                        color = InkSecondary.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal,
                    unfocusedBorderColor = Hairline,
                    focusedContainerColor = PaperWhite,
                    unfocusedContainerColor = PaperWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            ChipRow(
                items = filters,
                selectedIndex = state.filterIndex,
                onSelect = onFilter,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            if (state.rows.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.glossary_empty),
                        color = InkSecondary,
                        fontSize = 14.sp,
                        lineHeight = 19.sp,
                        letterSpacing = 1.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.rows) { row ->
                        GlossaryEntryRow(
                            row = row,
                            tone = tone,
                            mirrored = state.mirrored,
                            onOpenLetter = onOpenLetter
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlossaryEntryRow(
    row: GlossaryRow,
    tone: androidx.compose.ui.graphics.Color,
    mirrored: Boolean,
    onOpenLetter: (String) -> Unit
) {
    val pillColor = when (row.knowledge) {
        KnowledgeState.KNOWN -> Teal
        KnowledgeState.LEARNING -> Coral
        KnowledgeState.NEW -> Hairline
    }
    val pillText = when (row.knowledge) {
        KnowledgeState.KNOWN -> stringResource(R.string.pill_known)
        KnowledgeState.LEARNING -> stringResource(R.string.pill_learning)
        KnowledgeState.NEW -> stringResource(R.string.pill_new)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(PaperWhite, RoundedCornerShape(14.dp))
            .clickable { if (row.kind == EntryKind.LETTER) onOpenLetter(row.letter) }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (row.kind == EntryKind.LETTER) {
            HandshapeCanvas(
                spec = row.spec,
                size = 44.dp,
                tone = tone,
                description = stringResource(R.string.cd_handshape, row.letter),
                mirrored = mirrored
            )
        } else {
            SignMotionView(
                spec = row.spec,
                motion = row.motion,
                size = 48.dp,
                tone = tone,
                description = null,
                mirrored = mirrored
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = if (row.kind == EntryKind.LETTER) {
                stringResource(R.string.glossary_letter, row.letter)
            } else {
                stringResource(row.labelRes)
            },
            color = InkPrimary,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .background(pillColor.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = pillText,
                color = pillColor,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }
    }
}
