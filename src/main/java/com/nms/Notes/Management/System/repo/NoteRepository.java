package com.nms.Notes.Management.System.repo;

import com.nms.Notes.Management.System.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    @Query("{'$or': [{'title': {$regex: ?0, $options: 'i'}}, {'category': {$regex: ?0, $options: 'i'}}]}")
    Page<Note> searchNotes(String query, Pageable pageable);
}
