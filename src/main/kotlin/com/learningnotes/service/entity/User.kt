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
    var firstName: String? = null

    @Column(nullable = false)
    var lastName: String? = null

    @Column(unique = true, length = 100, nullable = false)
    var email: String? = null

    @Column(nullable = false)
    private var password: String? = null

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    var createdAt: Date? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = [JoinColumn(name = "user_id")])
    var authorities: MutableList<Authority>? = null

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

    fun setPassword(password: String?) {
        this.password = password
    }
}
