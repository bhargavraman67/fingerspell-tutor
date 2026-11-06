package com.fingerspell.tutor.data.repository

import com.fingerspell.tutor.data.local.TutorPreferences
import com.fingerspell.tutor.domain.model.LearnerProgress
import com.fingerspell.tutor.domain.repository.ProgressRepository

private const val KEY_BOXES = "boxes"
private const val KEY_DUE = "due_days"
private const val KEY_CONFUSIONS = "confusions"
private const val KEY_LESSONS = "lessons_done"
private const val KEY_BEST_STREAK = "best_streak"
private const val KEY_CORRECT = "lifetime_correct"
private const val KEY_TOTAL = "lifetime_total"
private const val MILLIS_PER_DAY = 86_400_000L

class ProgressRepositoryImpl(private val prefs: TutorPreferences) : ProgressRepository {

    override fun snapshot(): LearnerProgress = LearnerProgress(
        boxes = prefs.decodeCounts(prefs.text(KEY_BOXES)),
        dueDays = prefs.decodeCounts(prefs.text(KEY_DUE)),
        bestStreak = prefs.int(KEY_BEST_STREAK, 0),
        lifetimeCorrect = prefs.int(KEY_CORRECT, 0),
        lifetimeTotal = prefs.int(KEY_TOTAL, 0),
        lessonsDone = prefs.decodeIds(prefs.text(KEY_LESSONS)),
        confusions = prefs.decodeCounts(prefs.text(KEY_CONFUSIONS))
    )

    override fun save(progress: LearnerProgress) {
        prefs.putText(KEY_BOXES, prefs.encodeCounts(progress.boxes))
        prefs.putText(KEY_DUE, prefs.encodeCounts(progress.dueDays))
        prefs.putText(KEY_CONFUSIONS, prefs.encodeCounts(progress.confusions))
        prefs.putText(KEY_LESSONS, prefs.encodeIds(progress.lessonsDone))
        prefs.putInt(KEY_BEST_STREAK, progress.bestStreak)
        prefs.putInt(KEY_CORRECT, progress.lifetimeCorrect)
        prefs.putInt(KEY_TOTAL, progress.lifetimeTotal)
    }

    override fun today(): Int = (System.currentTimeMillis() / MILLIS_PER_DAY).toInt()

    override fun clear() {
        prefs.putText(KEY_BOXES, "")
        prefs.putText(KEY_DUE, "")
        prefs.putText(KEY_CONFUSIONS, "")
        prefs.putText(KEY_LESSONS, "")
        prefs.putInt(KEY_BEST_STREAK, 0)
        prefs.putInt(KEY_CORRECT, 0)
        prefs.putInt(KEY_TOTAL, 0)
    }
}
