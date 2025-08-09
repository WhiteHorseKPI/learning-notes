package com.learningnotes.service.response

import com.learningnotes.service.entity.Note

class NoteResponse(
    val id: Long,
    val title: String?,
    val description: String?,
    val isComplete: Boolean
) {
    companion object {
        fun fromNote(note: Note): NoteResponse {
            return NoteResponse(
                note.id,
                note.title,
                note.description,
                note.isComplete
            )
        }
    }
}