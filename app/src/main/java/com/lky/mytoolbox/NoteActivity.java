package com.lky.mytoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> notes;
    private NoteDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        recyclerView = findViewById(R.id.recycler_view_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        dbHelper = new NoteDatabaseHelper(this);

        // 检查数据库中是否有笔记，如果没有则添加默认笔记
        if (dbHelper.getAllNotes().isEmpty()) {
            addDefaultNotes();
        }

        refreshNotes();

        Button addNoteButton = findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNote();
            }
        });
    }

    private void addDefaultNotes() {
        // 添加三条默认笔记
        dbHelper.addNote(new Note(-1, "笔记1", "这是第一条默认笔记的内容。", new Date(), false, "", "", null, null, 0));
        dbHelper.addNote(new Note(-1, "笔记2", "这是第二条默认笔记的内容。", new Date(), false, "", "", null, null, 0));
        dbHelper.addNote(new Note(-1, "笔记3", "这是第三条默认笔记的内容。", new Date(), false, "", "", null, null, 0));
    }

    private void addNewNote() {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            refreshNotes();
        }
    }

    private void refreshNotes() {
        notes = dbHelper.getAllNotes();
        adapter = new NoteAdapter(notes, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra("note_id", note.getId());
        startActivityForResult(intent, 1);
    }
}
