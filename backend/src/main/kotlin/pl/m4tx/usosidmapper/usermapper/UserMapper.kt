package pl.m4tx.usosidmapper.usermapper

interface UserMapper {
    fun prefetchUsers(idList: List<String>)

    fun getUserById(id: String): User?
}
