package edu.cnm.deepdive.notekeeper.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import edu.cnm.deepdive.notekeeper.R;
import edu.cnm.deepdive.notekeeper.adapter.NoteAdapter;
import edu.cnm.deepdive.notekeeper.databinding.FragmentNoteBinding;
import edu.cnm.deepdive.notekeeper.viewmodel.NoteViewModel;

public class NoteFragment extends Fragment {

  private FragmentNoteBinding binding;
  private NoteViewModel viewModel;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentNoteBinding.inflate(inflater, container, false);
    binding.addNote.setOnClickListener(
        this::onClick);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
    viewModel
        .getNotes()
        .observe(getViewLifecycleOwner(),(notes) -> {
          NoteAdapter adapter = new NoteAdapter(getContext(), notes);
          binding.notes.setAdapter(adapter);
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private void onClick(View v) {
    Navigation.findNavController(binding.getRoot())
        .navigate(NoteFragmentDirections.openNote()); //generated for us from fragment
  }
}