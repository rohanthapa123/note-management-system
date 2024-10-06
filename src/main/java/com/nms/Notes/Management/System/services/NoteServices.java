package com.nms.Notes.Management.System.services;

import com.nms.Notes.Management.System.Exception.ResourceNotFoundException;
import com.nms.Notes.Management.System.controller.AddNote;
import com.nms.Notes.Management.System.model.Note;
import com.nms.Notes.Management.System.repo.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class NoteServices {

    private final NoteRepository noterepository;
    private final GoogleDriveService googledriveservice;

    public NoteServices(NoteRepository noterepository, GoogleDriveService googledriveservice) {
        this.noterepository = noterepository;
        this.googledriveservice = googledriveservice;
    }

    @Cacheable("notes")
    public Page<Note> getAllNotes(Integer pageNumber, Integer pageSize , String sortBy , String sortOrder){
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize , Sort.by(direction, sortBy));
        return noterepository.findAll(pageable);

    }

    public Page<Note> getSearchNotes(String query , Integer pageNumber, Integer pageSize , String sortBy , String sortOrder){
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        log.info(query);
        return noterepository.searchNotes(query, pageable);
    }

    @CacheEvict(value = "notes" , allEntries = true)
    public Note addNote(String title, String category, String imageurl , MultipartFile file) throws IOException , GeneralSecurityException {;
        Note newnote = new Note();
        newnote.setTitle(title);
        newnote.setCategory(category);
        newnote.setImage(imageurl);
        newnote.setUserId("66adf2af1ff55466c22c7a4b");
        newnote.setCreated_at(new Date());
        newnote.setUpdated_at(new Date());

        if (file != null && !file.isEmpty()) {
            String pdfFileId = googledriveservice.uploadFileToDrive(file, file.getContentType());
            String downloadUrl = "https://drive.google.com/uc?export=download&id=" + pdfFileId;
            String previewUrl = "https://drive.google.com/file/d/" + pdfFileId + "/preview";
            newnote.setDownload(downloadUrl);
            newnote.setUrl(previewUrl);
        }
        noterepository.save(newnote);
        getAllNotes(0,10,"created_at", "asc");
        return newnote;
    }

    @CacheEvict(value = "notes", allEntries = true)
    public Note updateNote(String id ,String title, String category, String imageurl, MultipartFile file, String existingPdf) throws GeneralSecurityException, IOException {

        Note oldnote = noterepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Note not found"));

        oldnote.setCategory(category);
        oldnote.setImage(imageurl);
        oldnote.setTitle(title);
        oldnote.setUpdated_at(new Date());

        if(existingPdf != null) {


            if (file != null && !file.isEmpty()) {
                String oldFileUrl = oldnote.getDownload();
                if (oldFileUrl != null && !oldFileUrl.isEmpty()) {
                    String oldFileId = extractFileIdFromUrl(oldFileUrl);
                    googledriveservice.deleteFileFromDrive(oldFileId);
                }
                String newFileId = googledriveservice.uploadFileToDrive(file, file.getContentType());
                String downloadUrl = "https://drive.google.com/uc?export=download&id=" + newFileId;
                String previewUrl = "https://drive.google.com/file/d/" + newFileId + "/preview";

                oldnote.setDownload(downloadUrl);
                oldnote.setUrl(previewUrl);
            }
        }
        return noterepository.save(oldnote);

    }

    @CacheEvict(value = "notes" , allEntries = true)
    public void deleteNoteById(String id) {
        noterepository.deleteById(id);
    }


    private String extractFileIdFromUrl(String fileUrl){
        String[] parts = fileUrl.split("id=");
        if(parts.length > 1){
            return parts[1];
        }
        return null;
    }

    public Note getNoteById(String id) {
        return noterepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No note found!!"));
    }
}
