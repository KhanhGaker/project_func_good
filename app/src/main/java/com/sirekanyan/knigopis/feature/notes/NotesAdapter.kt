package com.sirekanyan.knigopis.feature.notes

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.Router
import com.sirekanyan.knigopis.common.extensions.inflate
import com.sirekanyan.knigopis.repository.model.note.Note

class NotesAdapter(
    private val notes: List<Note>,
    private val router: Router
) : RecyclerView.Adapter<NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = parent.inflate(R.layout.note)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        val user = note.user
        val avatar = note.user.avatarUrl
        holder.setTitle(note.title)
        holder.setAuthor(note.author)
        holder.setNotes(note.notes)
        holder.setTimestamp(note.fixedCreatedAt.time)
        holder.setNickname(user.name)
        holder.view.setOnClickListener {
            router.openUserScreen(user.id, user.name, avatar)
        }
        holder.setAvatarUrl(avatar)
        holder.setBookImageUrl(note.bookImageUrl)
    }

}