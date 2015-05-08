package ru.alexeymz.web.model;

import ru.alexeymz.web.core.utils.EscapeUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Entity
@Table(name = "Comments")
public final class Comment {
    @Id
    @GeneratedValue
    private long id;
    private Calendar commentDate;
    private String username;
    private String text;

    public Comment() {}

    public Comment(Calendar commentDate, String username, String text) {
        this.commentDate = commentDate;
        this.username = username;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Calendar commentDate) {
        this.commentDate = commentDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "text='" + text + '\'' +
                ", username='" + username + '\'' +
                ", date=" + EscapeUtils.calendarToISO8601(commentDate) +
                ", id=" + id +
                '}';
    }
}
