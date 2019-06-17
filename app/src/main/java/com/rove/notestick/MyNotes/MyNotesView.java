package com.rove.notestick.MyNotes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.CRUDnote.CRUDnoteView;
import com.rove.notestick.R;
import com.rove.notestick.Util.DateParser;
import com.rove.notestick.Util.NoteContentParsor;

import java.util.Date;
import java.util.List;

public class MyNotesView extends Fragment {



    private MyNotesViewModel mViewModel;
    private RecyclerView MynotesRecyclerView;


    public static MyNotesView newInstance() {
        return new MyNotesView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_notes_fragment, container, false);
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyNotesViewModel.class);
        // TODO: Use the ViewModel
        MynotesRecyclerView = getActivity().findViewById(R.id.recyclerview);
        MynotesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final recyclerViewAdapter recyclerviewAdapter = new recyclerViewAdapter();
        MynotesRecyclerView.setAdapter(recyclerviewAdapter);
        mViewModel.getNotesByDate().observe(this, new Observer<List<Entity_Note>>() {
            @Override
            public void onChanged(List<Entity_Note> entity_notes) {

                recyclerviewAdapter.submitList(entity_notes);
            }
        });



    }
//    public MyNotesViewModel getmViewModel() {
//        return mViewModel;
    //}
    public void saveNote(){
        Entity_Note note = new Entity_Note();
        note.setDate(new Date());
//        note.setTitle("This is My Sample Title");
//        note.setContent("This is My Sample content which is more" +
//                "better when i wrap with somthing in the coming future.So we need to change " +
//                "that.");
//        note.setTitle("This is My Sample Title");
//        mViewModel.saveNote(note);

        Intent newnote = new Intent(getContext(), CRUDnoteView.class);
        newnote.putExtra(MyNotesViewModel.NEW_NOTE,note);
        startActivity(newnote);

    }

}




class recyclerViewAdapter extends ListAdapter<Entity_Note, recyclerViewAdapter.NotesViewHolder> {

    private static DiffUtil.ItemCallback<Entity_Note> DIFF_CALL_BACK = new DiffUtil.ItemCallback<Entity_Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Entity_Note oldItem, @NonNull Entity_Note newItem) {
            return oldItem.NoteId==newItem.NoteId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Entity_Note oldItem, @NonNull Entity_Note newItem) {
            return oldItem.getDate()==newItem.getDate()&&oldItem.getImgUrl()
                    .equals(newItem.getImgUrl())&&oldItem.getTitle().equals(newItem.getTitle())&&
                    oldItem.getContent().equals(newItem.getContent());
        }
    };

    protected recyclerViewAdapter() {
        super(DIFF_CALL_BACK);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_note_item, parent,
                false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        DateParser dateParser = new DateParser(getItem(position).getDate());
        holder.Date.setText(dateParser.getDate());
        holder.Month.setText(dateParser.getMonth());
        holder.Day.setText(dateParser.getDay());
        holder.Time.setText(dateParser.getTime());
        holder.Year.setText(dateParser.getYear());
        holder.Title.setText(getItem(position).getTitle());
        NoteContentParsor noteContentParsor = new NoteContentParsor(getItem(position).getContent());
        holder.Content.setText(noteContentParsor.getContentPreview());

    }


    class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView Date, Month, Day, Year, Time, Title, Content;
        private ImageView Thumbnail;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
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
