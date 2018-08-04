package pl.m4tx.usosidmapper.usermapper.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DbUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long = 0

    var userId: String = ""
    var firstName: String = ""
    var lastName: String = ""

    override fun toString(): String =
            "DbUser(id=$id, userId='$userId', firstName='$firstName', " +
                    "lastName='$lastName')"
}
