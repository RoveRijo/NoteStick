package com.rove.notestick.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.CRUDnote.CRUDnoteView;
import com.rove.notestick.MyNotes.MyNotesViewModel;
import com.rove.notestick.R;
import com.rove.notestick.Util.DateParser;
import com.rove.notestick.Util.NoteContentParsor;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteSelectionFrag extends DialogFragment {

    private List<Entity_Note> Dataset;
    private CalendarViewModel mViewModel;
    private RecyclerView recyclerView;

    public NoteSelectionFrag(List<Entity_Note> dataset) {
        Dataset = dataset;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.note_selection_frag, container, false);
        recyclerView = itemView.findViewById(R.id.recyclerview);
        return itemView;
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_note_item, parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            DateParser dateParser = new DateParser(Dataset.get(position).getDate());
            holder.Date.setText(dateParser.getDate());
            holder.Month.setText(dateParser.getMonth());
            holder.Day.setText(dateParser.getDay());
            holder.Time.setText(dateParser.getTime());
            holder.Year.setText(dateParser.getYear());
            holder.Title.setText(Dataset.get(position).getTitle());
            NoteContentParsor noteContentParsor = new NoteContentParsor(Dataset.get(position).getContent());
            holder.Content.setText(noteContentParsor.getContentPreview());
            holder.itemView.setOnClickListener(view -> {

                try {
                    showNoteEditorOnViewNoteMode(Dataset.get(position).getNoteId());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dismiss();

            });

        }

        @Override
        public int getItemCount() {
            return Dataset.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView Date, Month, Day, Year, Time, Title, Content;
            private ImageView Thumbnail;
            private View itemView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                Date = itemView.findViewById(R.id.date);
                Day = itemView.findViewById(R.id.day);
                Month = itemView.findViewById(R.id.month);
                Time = itemView.findViewById(R.id.time);
                Thumbnail = itemView.findViewById(R.id.thumbnail);
                Year = itemView.findViewById(R.id.year);
                Title = itemView.findViewById(R.id.title);
                Content = itemView.findViewById(R.id.content);
            }
        }

    }

    private void showNoteEditorOnViewNoteMode(int noteId) throws ExecutionException, InterruptedException {
        Entity_Note note = mViewModel.getNotWithId(noteId);
        Intent newnote = new Intent(getContext(), CRUDnoteView.class);
        newnote.putExtra(CRUDnoteView.MODE, CRUDnoteView.VIEW_NOTE_MODE);
        newnote.putExtra(MyNotesViewModel.VIEW_NOTE, note);
        startActivity(newnote);

    }
}
