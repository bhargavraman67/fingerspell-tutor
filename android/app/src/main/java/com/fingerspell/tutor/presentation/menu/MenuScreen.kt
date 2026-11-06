package com.fingerspell.tutor.presentation.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.MenuOverlay
import com.fingerspell.tutor.core.theme.Ochre
import com.fingerspell.tutor.core.theme.PaperWhite
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.PaperCard
import com.fingerspell.tutor.core.ui.PrimaryButton
import com.fingerspell.tutor.core.ui.ProgressStrip
import com.fingerspell.tutor.core.ui.RotatableHandshape
import com.fingerspell.tutor.core.ui.SecondaryButton
import com.fingerspell.tutor.core.ui.StatCard
import com.fingerspell.tutor.core.ui.TutorChip

@Composable
fun MenuRoute(
    onPlay: (List<String>) -> Unit,
    onReview: () -> Unit,
    onDestination: (Int, String) -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }
    MenuScreen(
        state = state,
        onPlay = { onPlay(state.lessonLetters) },
        onReview = onReview,
        onDestination = { index -> onDestination(index, state.heroLetter) }
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onReview: () -> Unit,
    onDestination: (Int) -> Unit
) {
    val navLabels = listOf(
        stringResource(R.string.lessons),
        stringResource(R.string.words),
        stringResource(R.string.mirror),
        stringResource(R.string.signs),
        stringResource(R.string.glossary),
        stringResource(R.string.culture)
    )
    val tone = skinTone(state.skinToneIndex)
    val statsVisible = listOf(state.knownCount, state.accuracy, state.bestStreak).count { it > 0 } >= 2

    AppBackdrop(overlay = MenuOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.home),
                    color = InkSecondary,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = { onDestination(6) }) {
                    Text(
                        text = stringResource(R.string.settings),
                        color = Coral,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.menu_welcome),
                        color = InkSecondary,
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = stringResource(R.string.menu_hands),
                        color = InkPrimary,
                        fontSize = 44.sp,
                        lineHeight = 48.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(56.dp)
                            .height(3.dp)
                            .background(Coral, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        color = InkSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        letterSpacing = 1.4.sp,
                        modifier = Modifier.widthIn(max = 200.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RotatableHandshape(
                        spec = state.heroSpec,
                        size = 132.dp,
                        tone = tone,
                        description = stringResource(R.string.cd_handshape_rotatable, state.heroLetter),
                        mirrored = state.mirrored
                    )
                    Text(
                        text = stringResource(R.string.menu_drag_hint),
                        color = InkSecondary.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                        letterSpacing = 1.6.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            PaperCard(innerPadding = 16) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.menu_lesson_number, state.lessonNumber),
                            color = Teal,
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(
                                R.string.menu_lesson_letters,
                                state.lessonLetters.joinToString(" ")
                            ),
                            color = InkPrimary,
                            fontSize = 18.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(44.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                ProgressStrip(fraction = state.knownCount / 26f)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.menu_known_of_total, state.knownCount),
                    color = InkSecondary,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
            if (statsVisible) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(
                        value = state.knownCount.toString(),
                        label = stringResource(R.string.stat_known),
                        accent = Teal,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = stringResource(R.string.value_percent, state.accuracy),
                        label = stringResource(R.string.stat_accuracy),
                        accent = Coral,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = state.bestStreak.toString(),
                        label = stringResource(R.string.stat_streak),
                        accent = Ochre,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (state.dueCount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PaperWhite, RoundedCornerShape(14.dp))
                        .border(BorderStroke(1.dp, Teal.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.menu_due_today, state.dueCount),
                        color = Teal,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = stringResource(R.string.review),
                        onClick = onReview
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = stringResource(R.string.menu_cta_subtitle),
                color = InkSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                iconRes = R.drawable.ic_play_arrow
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(navLabels.size) { index ->
                    TutorChip(
                        label = navLabels[index],
                        selected = false,
                        onClick = { onDestination(index) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
