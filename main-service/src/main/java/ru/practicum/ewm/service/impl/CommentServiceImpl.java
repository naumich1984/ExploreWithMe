package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model.dto.CommentDto;
import ru.practicum.ewm.model.dto.CommentFlatDto;
import ru.practicum.ewm.model.dto.NewCommentDto;
import ru.practicum.ewm.model.mapper.CommentMapper;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.CommentService;
import ru.practicum.ewm.utility.CommonPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto addCommentPrivate(NewCommentDto newCommentDto, Long userId, Long eventId) {
        log.debug("RUN addCommentPrivate");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new BadRequestException("Only published events can be commented on!");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(newCommentDto, userId, eventId));

        return CommentMapper.toCommentDto(comment, eventId, user);
    }

    @Override
    @Transactional
    public void deleteCommentPrivate(Long userId, Long commentId) {
        log.debug("RUN deleteCommentPrivate");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new BadRequestException("User with id=" + userId + " did not create a comment with id=" + commentId);
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteCommentAdmin(Long commentId) {
        log.debug("RUN deleteCommentPrivate");
        commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto updateCommentPrivate(NewCommentDto newCommentDto, Long userId, Long commentId) {
        log.debug("RUN updateCommentPrivate");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new BadRequestException("User with id=" + userId + " is not owner comment with id=" + commentId);
        }
        comment.setText(newCommentDto.getText());
        comment.setChanged(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(comment);
        Long eventId = updatedComment.getEvent().getId();

        return CommentMapper.toCommentDto(updatedComment, eventId, user);
    }

    @Override
    public List<CommentDto> getCommentsPublic(Long eventId, Integer from, Integer size,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, String text) {
        log.debug("RUN getCommentsPublic");
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        CommonPageRequest commonPageRequest = new CommonPageRequest(from, size);
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("The start date cannot be later than the end date");
        }

        List<CommentFlatDto> commentFlatDtos = commentRepository.findAllCommentsByFilter(eventId,
                rangeStart, rangeEnd, text, commonPageRequest);


        return commentFlatDtos.stream().map(CommentMapper::toCommentDtoFromFlat).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentPrivate(Long userId, Long commentId) {
        log.debug("RUN getCommentPrivate");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new BadRequestException("User with id=" + userId + " is not owner comment with id=" + commentId);
        }

        return CommentMapper.toCommentDto(comment, comment.getEvent().getId(), user);
    }

    @Override
    public CommentDto getCommentAdmin(Long commentId) {
        log.debug("RUN getCommentAdmin");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        return CommentMapper.toCommentDto(comment, comment.getEvent().getId(), comment.getAuthor());
    }
}
