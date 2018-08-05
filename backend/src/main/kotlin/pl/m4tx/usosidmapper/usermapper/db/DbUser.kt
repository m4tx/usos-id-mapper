package pl.m4tx.usosidmapper.usermapper.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class DbUser(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private var id: Long = 0,

        var userId: String = "",
        var firstName: String = "",
        var lastName: String = ""
)
