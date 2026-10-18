package com.gleb.repository;

import com.gleb.jooq.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.gleb.jooq.Tables.USERS;

@Repository
public class UserRepository {
    private final DSLContext jooq;

    public UserRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    public void save(UUID id, String name, String email) {
        jooq.insertInto(USERS)
                .set(USERS.ID, id)
                .set(USERS.NAME, name)
                .set(USERS.EMAIL, email)
                .execute();
    }

    public Optional<UsersRecord> findById(UUID id) {
        return jooq.selectFrom(USERS)
                .where(USERS.ID.eq(id)).
                fetchOptional();
    }

    public Optional<UsersRecord> findByEmail(String name) {
        return jooq.selectFrom(USERS)
                .where(USERS.EMAIL.eq(name))
                .fetchOptional();
    }
}
