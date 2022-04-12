package com.lukka.notifybackend.service;

import com.lukka.notifybackend.exception.EmptyRepositoryException;
import com.lukka.notifybackend.exception.ResourceNotFoundException;
import com.lukka.notifybackend.model.Note;
import com.lukka.notifybackend.repo.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {


    private final NoteRepo noteRepo;
    @Autowired
    public NoteService(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = noteRepo.findAll();
        if (!notes.isEmpty())
            return notes;
        else
            throw new EmptyRepositoryException("GET", "Note");
    }

    public Note getNote(Long id) {
        return noteRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Note", "Id", id));
    }

    public Note save(Note note) {
        note.setLastChange(note.getCreated());
        return noteRepo.save(note);
    }

    public void deleteNote(Long id) {
        noteRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Note", "Id", id));
        noteRepo.deleteById(id);
    }

    @Transactional
    public Note updateNote(Long id, Note note) {
        Note existingNote = noteRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Note", "Id", id));
        existingNote.setTitle(note.getTitle());
        existingNote.setBody(note.getBody());
        existingNote.setLastChange(LocalDateTime.now());
        return noteRepo.save(existingNote);
    }
}
