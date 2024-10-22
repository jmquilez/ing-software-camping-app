package es.unizar.eina.notepad.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.notepad.database.Note;
import es.unizar.eina.notepad.database.NoteRepository;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;

    private final LiveData<List<Note>> mAllNotes;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes() { return mAllNotes; }

    public void insert(Note note) { mRepository.insert(note); }

    public void update(Note note) { mRepository.update(note); }
    public void delete(Note note) { mRepository.delete(note); }
}
