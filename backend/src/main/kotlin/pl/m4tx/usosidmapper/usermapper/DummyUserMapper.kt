package pl.m4tx.usosidmapper.usermapper

class DummyUserMapper : UserMapper {
    override fun prefetchUsers(idList: List<String>) {}

    override fun getUserById(id: String): User? {
        if (id == "1136132") {
            return User("Mateusz", "Maćkowski")
        } else if (id == "1125642") {
            return User("Test", "User")
        }
        return null
    }
}
