package com.fingerspell.tutor.presentation.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.PaperWhite
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.ui.SignMotionView
import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.SignMotion

@Composable
fun SignDetailDialog(
    name: String,
    description: String,
    topic: String,
    spec: HandshapeSpec,
    motion: SignMotion,
    tone: Color,
    mirrored: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PaperWhite,
        shape = RoundedCornerShape(18.dp),
        title = {
            Text(
                text = name,
                color = InkPrimary,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SignMotionView(
                        spec = spec,
                        motion = motion,
                        size = 180.dp,
                        tone = tone,
                        description = stringResource(R.string.cd_sign_motion, name),
                        mirrored = mirrored
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    color = InkSecondary,
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.sign_detail_also, topic),
                    color = Teal,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.6.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    color = Coral,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    )
}
