package pl.m4tx.usosidmapper.usermapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.m4tx.usosidmapper.usermapper.db.DbUserMapper
import pl.m4tx.usosidmapper.usermapper.db.DbUserRepository

@Service
class DefaultUserMapperService : UserMapperService {
    @Autowired
    private lateinit var repository: DbUserRepository

    private lateinit var internalUserMapper: UserMapper
    override val userMapper: UserMapper
        get() {
            if (!::internalUserMapper.isInitialized) {
                internalUserMapper = DbUserMapper(repository)
            }
            return internalUserMapper
        }
}
