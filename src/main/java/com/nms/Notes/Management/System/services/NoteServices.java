package com.nms.Notes.Management.System.services;

import com.nms.Notes.Management.System.controller.AddNote;
import com.nms.Notes.Management.System.model.Note;
import com.nms.Notes.Management.System.repo.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

@Service
public class NoteServices {

    @Autowired
    NoteRepository noterepository;

    @Autowired
    GoogleDriveService googledriveservice;

    public List<Note> getAllNotes(){
        return noterepository.findAll();
    }

    public List<Note> getSearchNotes(String query){
        return noterepository.searchNotes(query);
    }

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



        return noterepository.save(newnote);
    }

    public void deleteNoteById(String id) {
        noterepository.deleteById(id);
    }


}
