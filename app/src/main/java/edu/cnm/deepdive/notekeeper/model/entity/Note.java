package edu.cnm.deepdive.notekeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(
    tableName = "note"
)
public class Note {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "note_id")
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created;

  @NonNull
  @ColumnInfo(index = true)
  private Date updated;

  @NonNull
  @ColumnInfo(index = true)
  private String subject;

  @NonNull
  private String text;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  @NonNull
  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(@NonNull Date updated) {
    this.updated = updated;
  }

  @NonNull
  public String getSubject() {
    return subject;
  }

  public void setSubject(@NonNull String subject) {
    this.subject = subject;
  }

  @NonNull
  public String getText() {
    return text;
  }

  public void setText(@NonNull String text) {
    this.text = text;
  }

}
