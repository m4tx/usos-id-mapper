package pl.m4tx.usosidmapper.usermapper.db

import org.springframework.data.repository.CrudRepository

interface DbUserRepository : CrudRepository<DbUser, Long> {
    fun findByUserIdIn(userIdList: List<String>): List<DbUser>
}
