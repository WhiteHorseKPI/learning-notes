package com.learningnotes.service.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


@Table(name = "users")
@Entity
class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    var id: Long = 0

    @Column(nullable = false)
    lateinit var firstName: String

    @Column(nullable = false)
    lateinit var lastName: String

    @Column(unique = true, length = 100, nullable = false)
    lateinit var email: String

    @Column(nullable = false)
    private lateinit var password: String

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private lateinit var createdAt: Date

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = [JoinColumn(name = "user_id")])
    private lateinit var authorities: MutableList<Authority>

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    private lateinit var notes: MutableList<Note>

    override fun getAuthorities(): MutableCollection<out GrantedAuthority?>? {
        return authorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return email
    }
}
