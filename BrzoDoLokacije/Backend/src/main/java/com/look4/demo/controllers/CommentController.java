package com.look4.demo.controllers;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.CommentDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/all")
    public List<CommentDTO> getAllComments(){
        return commentService.getAllComments();
    }

    @PostMapping("/post")
    public List<CommentDTO> getAllCommentsOnPost(@RequestBody SocialPostDTO socialPostDTO){
        return commentService.getAllCommentsOnPost(socialPostDTO);
    }
    @GetMapping("/user")
    public List<CommentDTO> getAllCommentsOfUser(@RequestBody AppUserDTO appUserDTO){
        return commentService.getAllCommentsByUser(appUserDTO);
    }
    @PostMapping("/count")
    public Long countCommentsOnPost(@RequestBody SocialPostDTO socialPostDTO){
        return commentService.countCommentsOnPost(socialPostDTO);
    }

    @PostMapping("")
    public CommentDTO addComment(@RequestBody CommentDTO commentDTO){
        return commentService.addComment(commentDTO);
    }

    @PatchMapping("")
    public CommentDTO updateComment(@RequestBody CommentDTO commentDTO){
        return commentService.updateComment(commentDTO);
    }
    @DeleteMapping("delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }
    
}
