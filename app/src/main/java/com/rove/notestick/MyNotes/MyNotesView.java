package com.rove.notestick.MyNotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.CRUDnote.CRUDnoteView;
import com.rove.notestick.R;
import com.rove.notestick.Util.DateParser;
import com.rove.notestick.Util.ImageSaver;
import com.rove.notestick.Util.NoteContentParsor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyNotesView extends Fragment {



    private MyNotesViewModel mViewModel;
    private RecyclerView MynotesRecyclerView;


    public static MyNotesView newInstance() {
        return new MyNotesView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        MynotesRecyclerView = getActivity().findViewById(R.id.recyclerview);
        MynotesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final recyclerViewAdapter recyclerviewAdapter = new recyclerViewAdapter(this);
        MynotesRecyclerView.setAdapter(recyclerviewAdapter);
        mViewModel.getNotesByDate();
        mViewModel.getNotesByDateAndQuery().observe(this, new Observer<List<Entity_Note>>() {
            @Override
            public void onChanged(List<Entity_Note> entity_notes) {

                recyclerviewAdapter.submitList(entity_notes);
            }
        });

    }



    public void showNoteEditorOnCreateMode(){
        Entity_Note note = new Entity_Note();
        note.setDate(new Date());
        Intent newnote = new Intent(getContext(), CRUDnoteView.class);
        newnote.putExtra(CRUDnoteView.MODE,CRUDnoteView.CREATE_NOTE_MODE);
        newnote.putExtra(MyNotesViewModel.NEW_NOTE,note);
        startActivity(newnote);

    }

    public void showNoteEditorOnViewNoteMode(int noteId) throws ExecutionException, InterruptedException {
        Entity_Note note = mViewModel.getNotWithId(noteId);
        Intent newnote = new Intent(getContext(), CRUDnoteView.class);
        newnote.putExtra(CRUDnoteView.MODE,CRUDnoteView.VIEW_NOTE_MODE);
        newnote.putExtra(MyNotesViewModel.VIEW_NOTE,note);
        startActivity(newnote);

    }

    public MyNotesViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.my_notes_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mViewModel.searchNotes(s);
                return true;
            }
        });
        MenuItem search = menu.findItem(R.id.search);
//        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                mViewModel.getNotesByDate();
//                return false;
//            }
//        });
        super.onCreateOptionsMenu(menu, inflater);
    }


}




class recyclerViewAdapter extends ListAdapter<Entity_Note, recyclerViewAdapter.NotesViewHolder> {

    private ActionMode actionMode;
    private MyNotesView myNotesView;

    private void deleteNote(Entity_Note note){
        myNotesView.getViewModel().deleteNote(note);
    }

    private static DiffUtil.ItemCallback<Entity_Note> DIFF_CALL_BACK = new DiffUtil.ItemCallback<Entity_Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Entity_Note oldItem, @NonNull Entity_Note newItem) {
            return oldItem.NoteId==newItem.NoteId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Entity_Note oldItem, @NonNull Entity_Note newItem) {
            return oldItem.getDate().equals(newItem.getDate())&&oldItem.getImgUrl()
                    .equals(newItem.getImgUrl())&&oldItem.getTitle().equals(newItem.getTitle())&&
                    oldItem.getContent().equals(newItem.getContent());
        }
    };

    protected recyclerViewAdapter(MyNotesView myNotesView) {
        super(DIFF_CALL_BACK);
        this.myNotesView = myNotesView;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_note_item, parent,
                false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, final int position) {
        DateParser dateParser = new DateParser(getItem(position).getDate());
        holder.Date.setText(dateParser.getDate());
        holder.Month.setText(dateParser.getMonth());
        holder.Day.setText(dateParser.getDay());
        holder.Time.setText(dateParser.getTime());
        holder.Year.setText(dateParser.getYear());
        holder.Title.setText(getItem(position).getTitle());
        NoteContentParsor noteContentParsor = new NoteContentParsor(getItem(position).getContent());
        holder.Content.setText(noteContentParsor.getContentPreview());
        if(noteContentParsor.getImageSrcs().size()>0){
            holder.Thumbnail.setImageBitmap(myNotesView.getViewModel().getThumbnail(getItem(position).
                    getImgUrl()));
        }
        else{
            holder.Thumbnail.setImageBitmap(null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myNotesView.showNoteEditorOnViewNoteMode(getItem(position).getNoteId());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(actionMode==null){
                    actionMode = myNotesView.getActivity().startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionmode, Menu menu) {
                            actionmode.getMenuInflater().inflate(R.menu.mynotes_contextual_menu,menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.delete:
                                    deleteNote(getItem(position));
                                    return true;
                                case R.id.edit:
                                    return true;
                                default:return false;
                            }
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionmode) {
                            actionMode = null;

                        }
                    });
                }
                else return false;

                return true;
            }
        });


    }


    class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView Date, Month, Day, Year, Time, Title, Content;
        private ImageView Thumbnail;
        private View itemView;

        public NotesViewHolder(@NonNull View itemView) {
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
