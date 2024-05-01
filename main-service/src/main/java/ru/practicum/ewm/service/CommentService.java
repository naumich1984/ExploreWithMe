package ru.practicum.ewm.service;

import ru.practicum.ewm.model.dto.CommentDto;
import ru.practicum.ewm.model.dto.NewCommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentDto addCommentPrivate(NewCommentDto newCommentDto, Long userId, Long eventId);

    void deleteCommentPrivate(Long userId, Long commentId);

    void deleteCommentAdmin(Long commentId);

    CommentDto updateCommentPrivate(NewCommentDto newCommentDto, Long userId, Long commentId);

    List<CommentDto> getCommentsPublic(Long eventId, Integer from, Integer size,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, String text);

    CommentDto getCommentPrivate(Long userId, Long commentId);

    CommentDto getCommentAdmin(Long commentId);
}
