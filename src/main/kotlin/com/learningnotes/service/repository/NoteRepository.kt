package com.learningnotes.service.repository

import com.learningnotes.service.entity.Note
import com.learningnotes.service.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface NoteRepository : CrudRepository<Note, Long> {
    fun findByOwner(owner: User): List<Note>
}