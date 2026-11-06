package com.fingerspell.tutor.presentation.lessonpath

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.fingerspell.tutor.core.ui.HandshapeCanvas
import com.fingerspell.tutor.core.ui.PaperCard
import com.fingerspell.tutor.core.ui.SectionHeader
import com.fingerspell.tutor.core.ui.TopBar

@Composable
fun LessonPathRoute(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onOpenLetter: (String) -> Unit,
    onDrill: (List<String>) -> Unit,
    onOpenSigns: () -> Unit
) {
    val lessonPathViewModel: LessonPathViewModel = viewModel(factory = ServiceLocator.factory)
    val state by lessonPathViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        lessonPathViewModel.refresh()
    }
    LessonPathScreen(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onOpenLetter = onOpenLetter,
        onDrill = onDrill,
        onOpenSigns = onOpenSigns
    )
}

@Composable
fun LessonPathScreen(
    state: LessonPathUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onOpenLetter: (String) -> Unit,
    onDrill: (List<String>) -> Unit,
    onOpenSigns: () -> Unit
) {
    val tone = skinTone(state.skinToneIndex)
    AppBackdrop(overlay = ContentOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            TopBar(
                title = stringResource(R.string.lesson_path_title),
                onBack = onBack,
                onHome = onHome,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.lesson_path_counter, state.knownCount),
                    color = Teal,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SectionHeader(
                            text = stringResource(R.string.unit_alphabet),
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = painterResource(R.drawable.sprite_4),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                items(state.rows.filter { it.unit == 1 }) { row ->
                    LessonCard(
                        row = row,
                        tone = tone,
                        mirrored = state.mirrored,
                        onOpen = { onOpenLetter(row.letters.first()) },
                        onDrill = { onDrill(row.letters) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SectionHeader(
                            text = stringResource(R.string.unit_signs),
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = painterResource(R.drawable.sprite_5),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                items(state.rows.filter { it.unit == 2 }) { row ->
                    TopicCard(
                        row = row,
                        tone = tone,
                        mirrored = state.mirrored,
                        onOpen = onOpenSigns
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonCard(
    row: LessonRow,
    tone: androidx.compose.ui.graphics.Color,
    mirrored: Boolean,
    onOpen: () -> Unit,
    onDrill: () -> Unit
) {
    PaperCard(innerPadding = 12, modifier = Modifier.clickable(onClick = onOpen)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        when {
                            row.complete -> Teal
                            row.current -> PaperWhite
                            else -> Hairline
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = row.id.toString(),
                    color = if (row.complete) PaperWhite else InkPrimary,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(
                        R.string.menu_lesson_letters,
                        row.letters.joinToString(" ")
                    ),
                    color = InkPrimary,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.lesson_letter_count),
                    color = InkSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    letterSpacing = 1.sp
                )
            }
            Spacer(
                modifier = Modifier
                    .width(6.dp)
                    .height(36.dp)
                    .background(if (row.current) Coral else Hairline, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            HandshapeCanvas(
                spec = row.spec,
                size = 36.dp,
                tone = tone,
                description = stringResource(R.string.cd_handshape, row.letters.first()),
                mirrored = mirrored
            )
        }
        TextButton(onClick = onDrill) {
            Text(
                text = stringResource(R.string.drill),
                color = Coral,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.6.sp
            )
        }
    }
}

@Composable
private fun TopicCard(
    row: LessonRow,
    tone: androidx.compose.ui.graphics.Color,
    mirrored: Boolean,
    onOpen: () -> Unit
) {
    PaperCard(innerPadding = 12, modifier = Modifier.clickable(onClick = onOpen)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HandshapeCanvas(
                spec = row.spec,
                size = 40.dp,
                tone = tone,
                description = null,
                mirrored = mirrored
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(row.topicLabelRes),
                    color = InkPrimary,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = stringResource(R.string.lesson_sign_count),
                    color = InkSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    letterSpacing = 1.sp
                )
            }
            Spacer(
                modifier = Modifier
                    .width(6.dp)
                    .height(32.dp)
                    .background(Teal.copy(alpha = 0.6f), RoundedCornerShape(3.dp))
            )
        }
    }
}
