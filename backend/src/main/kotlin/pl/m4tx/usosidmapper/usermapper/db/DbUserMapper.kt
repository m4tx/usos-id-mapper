package pl.m4tx.usosidmapper.usermapper.db

import pl.m4tx.usosidmapper.usermapper.User
import pl.m4tx.usosidmapper.usermapper.UserMapper

class DbUserMapper(
        private val dbUserRepository: DbUserRepository
) : UserMapper {
    private val map = HashMap<String, User?>()

    override fun prefetchUsers(idList: List<String>) {
        // Make sure that non-existing users are not retrieved from the DB again
        idList.stream()
                .filter { id -> id !in map }
                .forEach { id -> map[id] = null }

        // Only retrieve users that are not prefetched yet
        val idListFiltered = idList.filter { id -> id !in map }
        if (!idListFiltered.isEmpty()) {
            val users = dbUserRepository.findByUserIdIn(idListFiltered)
            users.forEach { dbUser ->
                map[dbUser.userId] = dbUserToUser(dbUser)
            }
        }
    }

    private fun dbUserToUser(dbUser: DbUser): User =
            User(dbUser.firstName, dbUser.lastName)

    override fun getUserById(id: String): User? {
        if (id !in map) {
            prefetchUsers(listOf(id))
        }
        return map[id]
    }
}
