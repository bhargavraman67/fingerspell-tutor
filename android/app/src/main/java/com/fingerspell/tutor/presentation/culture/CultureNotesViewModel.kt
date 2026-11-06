package com.fingerspell.tutor.presentation.culture

import androidx.lifecycle.ViewModel
import com.fingerspell.tutor.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CultureNote(
    val titleRes: Int,
    val bodyRes: Int
)

data class CultureNotesUiState(
    val notes: List<CultureNote> = emptyList()
)

class CultureNotesViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        CultureNotesUiState(
            notes = listOf(
                CultureNote(R.string.culture_1_title, R.string.culture_1_body),
                CultureNote(R.string.culture_2_title, R.string.culture_2_body),
                CultureNote(R.string.culture_3_title, R.string.culture_3_body),
                CultureNote(R.string.culture_4_title, R.string.culture_4_body),
                CultureNote(R.string.culture_5_title, R.string.culture_5_body),
                CultureNote(R.string.culture_6_title, R.string.culture_6_body)
            )
        )
    )
    val state: StateFlow<CultureNotesUiState> = _state.asStateFlow()
}
