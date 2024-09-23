package com.nms.Notes.Management.System.controller;

import com.nms.Notes.Management.System.model.Note;
import com.nms.Notes.Management.System.services.NoteServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@CrossOrigin
@RestController()
@RequestMapping("/api")
@Slf4j
public class NoteController {

    @Autowired
    private NoteServices noteservices;

    @GetMapping("/notes")
    public List<Note> getAllNotes(){
        return noteservices.getAllNotes();
    }


    @GetMapping("/notes/search")
    public List<Note> getSearchNotes(@RequestParam(value = "query" , required = false , defaultValue = "") String query){
        return noteservices.getSearchNotes(query);
    }

    @PostMapping("/note")
    public Note addNote(
            @RequestPart("title") String title,
            @RequestPart("category") String category,
            @RequestPart("noteimage") String noteimage,
            @RequestPart("notepdf") MultipartFile notepdf
    ) {
        try {
            return noteservices.addNote(title, category , noteimage, notepdf);
        }catch (Exception e){
            log.info(e.getMessage());
            return new Note();
        }
    }

    @PutMapping("note/{id}")
    public Note updateNote(
            @PathVariable String id,
            @RequestPart("title") String title,
            @RequestPart("category") String category,
            @RequestPart("noteimage") String noteimage,
            @RequestPart("notepdf") MultipartFile notepdf) throws GeneralSecurityException, IOException {

        return noteservices.updateNote(id, title, category,noteimage, notepdf);
    }

    @DeleteMapping("note/{id}")
    public void deleteNoteById(@PathVariable String id) {
        noteservices.deleteNoteById(id);
    }
}
