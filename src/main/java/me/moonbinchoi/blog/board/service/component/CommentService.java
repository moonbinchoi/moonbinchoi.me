package me.moonbinchoi.blog.board.service.component;

import lombok.RequiredArgsConstructor;
import me.moonbinchoi.blog.board.domain.Comment;
import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.dto.request.CommentRequest;
import me.moonbinchoi.blog.board.service.module.CommentCrudService;
import me.moonbinchoi.blog.board.service.module.PostCrudService;
import me.moonbinchoi.blog.global.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentCrudService commentCrudService;
    private final PostCrudService postCrudService;

    public Comment createComment(CommentRequest.Create request) {
        final Long postId = request.getPostId();
        final Post foundPost = EntityUtils.getEntityOrThrow(postCrudService.findById(postId), postId);
        final Comment savedComment = commentCrudService.save(request.toEntity(foundPost));

        foundPost.addComment(savedComment);
        return savedComment;
    }

    public Comment createReply(CommentRequest.CreateReply request) {
        final Long postId = request.getPostId();
        final Post foundPost = EntityUtils.getEntityOrThrow(postCrudService.findById(postId), postId);
        final Comment parentComment = findComment(request.getParentCommentId());
        final Comment savedComment = commentCrudService.save(request.toEntity(foundPost, parentComment));

        parentComment.addChild(savedComment);
        foundPost.addComment(savedComment);
        return savedComment;
    }

    public Comment findComment(Long id) {
        return EntityUtils.getEntityOrThrow(commentCrudService.findById(id), id);
    }

    public Comment updateComment(CommentRequest.Update request) {
        return commentCrudService.update(request.getCommentId(), request.toEntity());
    }

    public void deleteComment(CommentRequest.Delete request) {
        final Post foundPost = EntityUtils.getEntityOrThrow(
                postCrudService.findById(request.getPostId()), request.getPostId());
        if (!Objects.equals(foundPost.getId(), request.getPostId())) {
            throw new IllegalArgumentException(
                    String.format("Requested Comment's Post ID %s and actual Post ID %s is different.",
                            request.getPostId(), foundPost.getId()));
        }
        final Comment foundComment = EntityUtils.getEntityOrThrow(
                commentCrudService.findById(request.getCommentId()), request.getCommentId());

        foundPost.removeComment(foundComment);
        if (foundComment.childExists()) {
            foundComment.softDelete();
            return;
        }
        commentCrudService.deleteById(getDeletableParentComment(foundComment).getId());
    }

    private Comment getDeletableParentComment(Comment comment) {
        final Comment parent = comment.getParent();
        if (parent != null && parent.childCount() == 1 && parent.getIsDeleted()) {
            /*
             * Conditions to ascend parent comment
             * 1. this comment is not orphan
             * 2. parent.children has ONLY THIS comment
             * 3. parent.isDeleted == true
             */
            return getDeletableParentComment(parent);
        }
        // Delete this comment also can delete CASCADE child comments
        return comment;
    }
}
