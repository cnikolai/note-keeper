package edu.cnm.deepdive.notekeeper.service;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.notekeeper.model.dao.NoteDao;
import edu.cnm.deepdive.notekeeper.model.entity.Note;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.List;

public class NoteRepository {

  private final Application context;
  private final NoteDao noteDao;

  public NoteRepository(Application context) {
    this.context = context;
    noteDao = NoteKeeperDatabase
        .getInstance()
        .getNoteDao();
  }

  public LiveData<Note> get(long noteId) { //because it's livedata, it runs in the background!
    return noteDao.select(noteId);
  }

  public LiveData<List<Note>> getAll() {
    return noteDao.select();
  }

  public Single<Note> save(Note note) {
    Single<Note> task;
    note.setUpdated(new Date());
    if (note.getId() == 0) {
      note.setCreated(note.getUpdated());
      task = noteDao
          .insert(note)
          .map((id) -> {
            note.setId(id);
            return note;
          }); //id of record added, then puts note back on the conveyor belt
    } else {
      task = noteDao
          .update(note)
          .map((count) -> //count of records modified, then puts note back on the conveyor belt
            note
          );
    }
    return task.subscribeOn(Schedulers.io()); //give task back to viewModel to subscribe to (background thread)
  }

  public Completable delete(Note note) {
    return (note.getId() == 0) //if id is 0, then the note has not been saved in the database, no point in deleting it
        ? Completable.complete()
        : noteDao
            .delete(note)
            .ignoreElement() //not interested in how many records deleted; .ignore turns from single to completable.  or .flatmap (takes single and daisy chains completable to it)
            .subscribeOn(Schedulers.io());
  }
}
