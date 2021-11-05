package edu.cnm.deepdive.notekeeper.adapter;

import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.notekeeper.databinding.ItemNoteBinding;
import edu.cnm.deepdive.notekeeper.model.entity.Note;
import java.text.DateFormat;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.Holder> {

  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final List<Note> notes;

  public NoteAdapter(Context context, List<Note> notes) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.notes = notes;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemNoteBinding.inflate(inflator, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return notes.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemNoteBinding binding;

    private Holder(@NonNull ItemNoteBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      //Use contents of model object to set contents of binding fields.
      Note note = notes.get(position);
      binding.subject.setText(note.getSubject());
      binding.updated.setText(dateFormat.format(note.getUpdated()));
      binding.text.setText(note.getText());
    }
  }
}
