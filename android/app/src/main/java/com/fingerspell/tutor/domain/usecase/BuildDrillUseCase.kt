package com.fingerspell.tutor.domain.usecase

import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.DrillMode
import com.fingerspell.tutor.domain.model.DrillQuestion
import com.fingerspell.tutor.domain.model.Handshape
import com.fingerspell.tutor.domain.model.LearnerProgress
import kotlin.random.Random

class BuildDrillUseCase {

    operator fun invoke(
        mode: DrillMode,
        handshapes: List<Handshape>,
        progress: LearnerProgress,
        today: Int,
        random: Random = Random.Default
    ): List<DrillQuestion> {
        val byLetter = handshapes.associateBy { it.letter }
        val alphabet = handshapes.map { it.letter }
        val raw = when (mode) {
            is DrillMode.LessonSet -> expand(mode.letters, progress, alphabet)
            DrillMode.Review -> {
                val due = progress.dueDays.filter { it.value <= today }.keys.toList()
                if (due.size < 4) alphabet.take(9) else due
            }
            DrillMode.Mixed -> {
                val seen = progress.boxes.keys.toList()
                if (seen.size < 4) alphabet.take(9) else seen
            }
        }
        val pool = raw.distinct().ifEmpty { alphabet.take(9) }
        return (0 until GameConfig.QUESTIONS_PER_DRILL).map {
            val target = pool[random.nextInt(pool.size)]
            DrillQuestion(target = target, options = options(target, byLetter, alphabet, random))
        }
    }

    private fun expand(letters: List<String>, progress: LearnerProgress, alphabet: List<String>): List<String> {
        val known = progress.boxes.filter { it.value >= 2 }.keys
        val merged = letters + known.filter { it !in letters }.take(4)
        return if (merged.size >= 4) merged else alphabet.take(6)
    }

    private fun options(
        target: String,
        byLetter: Map<String, Handshape>,
        alphabet: List<String>,
        random: Random
    ): List<String> {
        val picked = linkedSetOf(target)
        byLetter[target]?.confusables?.forEach {
            if (picked.size < GameConfig.OPTIONS_PER_QUESTION) picked.add(it)
        }
        var guard = 0
        while (picked.size < GameConfig.OPTIONS_PER_QUESTION && guard < 300) {
            picked.add(alphabet[random.nextInt(alphabet.size)])
            guard++
        }
        return picked.toList().shuffled(random)
    }
}
