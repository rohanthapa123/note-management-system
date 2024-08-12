package com.nms.Notes.Management.System.repo;

import com.nms.Notes.Management.System.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByEmail(String email);
}
