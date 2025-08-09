package com.learningnotes.service.controller

import com.learningnotes.service.request.NoteRequest
import com.learningnotes.service.response.NoteResponse
import com.learningnotes.service.service.NoteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@Tag(name = "Note REST API Endpoints", description = "Operations for managing user notes")
@RestController
@RequestMapping("/api/notes")
class NoteController(private val noteService: NoteService) {

    @Operation(summary = "Create note for user", description = "Create note for the authenticated user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createNote(@RequestBody @Valid noteRequest: NoteRequest): NoteResponse {
        return noteService.createNote(noteRequest)
    }

    @Operation(summary = "Get all notes for user", description = "Fetch all notes for the authenticated user")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun getAllNotes(): List<NoteResponse> {
        return noteService.getAllNotes()
    }

    @Operation(summary = "Change note completion", description = "Update note completion to opposite")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    fun toggleNoteCompletion(@PathVariable @Min(1) id: Long): NoteResponse {
        return noteService.toggleNoteCompletion(id)
    }

    @Operation(summary = "Delete note for user", description = "Delete note for the authenticated user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable @Min(1) id: Long) {
        noteService.deleteNote(id)
    }
}