package edu.cnm.deepdive.notekeeper.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import edu.cnm.deepdive.notekeeper.model.entity.Note;
import edu.cnm.deepdive.notekeeper.service.NoteRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class NoteViewModel
    extends AndroidViewModel
    implements LifecycleObserver {

  private final NoteRepository repository;
  private final MutableLiveData<Throwable> throwable;
  private final MutableLiveData<Long> noteId;
  private final LiveData<Note> note;
  private final CompositeDisposable pending; //when working with reactiveX

  public NoteViewModel(@NonNull Application application) {
    super(application);
    repository = new NoteRepository(application);
    throwable = new MutableLiveData<>(); //figure out generic type from where it is declared above.
    noteId = new MutableLiveData<>();
    //whenever the contents of this noteid changes, it invokes the lambda
    note = Transformations.switchMap(noteId, repository::get
        //but query doesn't execute unless someone is observing that livedata.
    );//triggers a refresh of live data
    pending = new CompositeDisposable(); // a bucket to hold reactive x tasks that are in process and if UI goes away, then can empty that bucket and cancel those tasks in waiting; when do a subscribe return type of subscribe goes to consumer and is a disposable and goes to ???
  }

  public LiveData<Note> getNote() {
    return note;
  }

  public void setNoteId(long id) {
    noteId.setValue(id);//if someone is observing this, it will cause a refresh of note assignment in constructor
  }

  public LiveData<List<Note>> getNotes() {
    return repository.getAll();
  }

  public MutableLiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void save(Note note) {
    pending.add(
        repository
            .save(note)
            .subscribe(
                (savedNote) -> {},
                this::postThrowable //replace expression lambda with method reference lambda.
            ) //causes this to execute; first parameter is consume of succesful results, second is consumer of unsucessful result
    );
  }

  public void delete(Note note) {
    pending.add(
        repository
            .delete(note)
            .subscribe(
                () -> {},
                this::postThrowable
            )//first one is completable - action that does nothing, consume of a throwable
    );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }
}
