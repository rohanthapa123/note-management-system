package com.nms.Notes.Management.System.controller;

import com.nms.Notes.Management.System.model.Note;
import com.nms.Notes.Management.System.services.NoteServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@CrossOrigin
@RestController()
@RequestMapping("/api")
@Slf4j
public class NoteController {

    @Autowired
    private NoteServices noteservices;

    @GetMapping("/notes")
    public Page<Note> getAllNotes(
            @RequestParam(value = "pageNumber" , defaultValue = "0" , required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "10" , required = false) Integer pageSize,
            @RequestParam(value = "sortBy" , defaultValue = "created_at" , required = false) String sortBy,
            @RequestParam(value = "sortOrder" , defaultValue = "asc" , required = false) String sortOrder
    ){
        return noteservices.getAllNotes(pageNumber, pageSize , sortBy , sortOrder);
    }


    @GetMapping("/notes/search")
    public Page<Note> getSearchNotes(
            @RequestParam(value = "query" , required = false , defaultValue = "") String query,
            @RequestParam(value = "pageNumber" , defaultValue = "0" , required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "10" , required = false) Integer pageSize,
            @RequestParam(value = "sortBy" , defaultValue = "created_at" , required = false) String sortBy,
            @RequestParam(value = "sortOrder" , defaultValue = "asc" , required = false) String sortOrder
    ){
        return noteservices.getSearchNotes(query , pageNumber, pageSize , sortBy , sortOrder);
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
