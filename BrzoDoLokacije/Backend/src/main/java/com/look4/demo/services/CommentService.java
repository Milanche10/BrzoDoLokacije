package com.look4.demo.services;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.CommentDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.dto.mappers.AppUserMapper;
import com.look4.demo.dto.mappers.CommentMapper;
import com.look4.demo.dto.mappers.SocialPostMapper;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.Comment;
import com.look4.demo.entities.SocialPost;
import com.look4.demo.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    SocialPostMapper socialPostMapper;
    @Autowired
    AppUserMapper appUserMapper;

    public CommentDTO addComment(CommentDTO commentDTO){
        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    public Long countCommentsOnPost(SocialPostDTO socialPostDTO){
        SocialPost socialPost = socialPostMapper.toEntity(socialPostDTO);
        return commentRepository.countCommentBySocialPost(socialPost);
    }

    public List<CommentDTO> getAllComments(){
        return commentRepository.findAll().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    public List<CommentDTO> getAllCommentsOnPost(SocialPostDTO socialPostDTO){
        SocialPost socialPost = socialPostMapper.toEntity(socialPostDTO);
        return commentRepository.getCommentsBySocialPost(socialPost).stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    public List<CommentDTO> getAllCommentsByUser(AppUserDTO appUserDTO){
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        return commentRepository.getCommentsByAppUser(appUser).stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    public CommentDTO updateComment(CommentDTO commentDTO){
        return commentMapper.toDto(commentRepository.save(commentMapper.toEntity(commentDTO)));
    }

    public void deleteComment(Long commentId){
        boolean exists = commentRepository.existsById(commentId);
        if(!exists){
            throw new IllegalStateException("Comment reaction with id "+commentId+" does not exist");
        }
        commentRepository.deleteById(commentId);
    }




}
