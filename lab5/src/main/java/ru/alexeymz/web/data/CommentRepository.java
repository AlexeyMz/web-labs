package ru.alexeymz.web.data;

import ru.alexeymz.web.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    public static final String ATTRIBUTE = "COMMENT_REPOSITORY";

    List<Comment> findCommentFrom(Optional<Long> commentId);
    void save(Comment comment);
}
