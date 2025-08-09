package com.learningnotes.service.service

import com.learningnotes.service.entity.Note
import com.learningnotes.service.repository.NoteRepository
import com.learningnotes.service.request.NoteRequest
import com.learningnotes.service.response.NoteResponse
import com.learningnotes.service.util.UserUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class NoteService(private val noteRepository: NoteRepository) {

    @Transactional
    fun createNote(noteRequest: NoteRequest): NoteResponse {
        val currentUser = UserUtils.getAuthenticatedUser()
        val note = Note(
            title = noteRequest.title,
            description = noteRequest.description,
            complete = false,
            owner = currentUser
        )
        val savedNote: Note = noteRepository.save(note)
        return NoteResponse.fromNote(savedNote)
    }

    @Transactional(readOnly = true)
    fun getAllNotes(): List<NoteResponse> {
        val currentUser = UserUtils.getAuthenticatedUser()
        return noteRepository.findByOwner(currentUser)
            .stream()
            .map { NoteResponse.fromNote(it) }
            .toList()
    }

    @Transactional
    fun toggleNoteCompletion(id: Long): NoteResponse {
        val currentUser = UserUtils.getAuthenticatedUser()
        val note: Optional<Note> = noteRepository.findByIdAndOwner(id, currentUser)
        if (note.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Note is not found")
        }
        val notNullNote = note.get()
        notNullNote.isComplete = !notNullNote.isComplete
        val updatedNote = noteRepository.save(notNullNote)
        return NoteResponse.fromNote(updatedNote)
    }

    @Transactional
    fun deleteNote(id: Long) {
        val currentUser = UserUtils.getAuthenticatedUser()
        val note: Optional<Note> = noteRepository.findByIdAndOwner(id, currentUser)
        if (note.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found")
        }
        noteRepository.delete(note.get())
    }
}