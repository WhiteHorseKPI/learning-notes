package com.learningnotes.service.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.Date


@Table(name = "notes")
@Entity
class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    var id: Long = 0

    @Column(nullable = false)
    var title: String? = null

    @Column(nullable = false)
    var description: String? = null

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private var createdAt: Date? = null

    @UpdateTimestamp
    @Column(name = "updated_at")
    private var updatedAt: Date? = null

    @Column(nullable = false)
    var isComplete: Boolean = false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private var owner: User? = null

    constructor(title: String?, description: String?, complete: Boolean, owner: User?) {
        this.title = title
        this.description = description
        this.isComplete = complete
        this.owner = owner
    }

    fun getOwner(): User? {
        return owner
    }

    fun setOwner(owner: User?) {
        this.owner = owner
    }
}
