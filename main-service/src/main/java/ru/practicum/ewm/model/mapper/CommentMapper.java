package ru.practicum.ewm.model.mapper;

import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.dto.CommentDto;
import ru.practicum.ewm.model.dto.CommentFlatDto;
import ru.practicum.ewm.model.dto.NewCommentDto;
import ru.practicum.ewm.model.dto.UserShortDto;

public class CommentMapper {

    public static Comment toComment(NewCommentDto newCommentDto, Long userId, Long eventId) {

        return Comment.builder().text(newCommentDto.getText())
                .event(Event.builder().id(eventId).build())
                .author(User.builder().id(userId).build())
                .created(null)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment, Long eventId, User user) {

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserShortDto(user))
                .event(eventId)
                .created(comment.getCreated())
                .changed(comment.getChanged())
                .build();
    }

    public static CommentDto toCommentDtoFromFlat(CommentFlatDto commentFlatDto) {

        return CommentDto.builder()
                .id(commentFlatDto.getId())
                .event(commentFlatDto.getEvent())
                .author(UserShortDto.builder().id(commentFlatDto.getAuthorId()).name(commentFlatDto.getAuthorName()).build())
                .created(commentFlatDto.getCreated())
                .changed(commentFlatDto.getChanged())
                .text(commentFlatDto.getText())
                .build();
    }
}
