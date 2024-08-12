package com.pro.controller;

import com.pro.entity.Comment;

import com.pro.entity.Post;
import com.pro.entity.User;
import com.pro.repo.CommentRepository;
import com.pro.repo.PostRepository;
import com.pro.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/post/{pid}/addComment")
    public ModelAndView addComment(@PathVariable("pid") int postId,
                             @ModelAttribute("comment") Comment comment,
                             Model model) {
        // Retrieve the post based on postId
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        // Assume you have a method to get the currently logged-in user
        User user = getCurrentLoggedInUser();

        // Set the post and user in the comment
        comment.setPost(post);
        comment.setUser(user);

        // Save the comment
        commentRepository.save(comment);
        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/about");
		return modelAndView;

    }

    // Mock method to get the currently logged-in user
    private User getCurrentLoggedInUser() {
        // You can replace this with the actual logic to retrieve the logged-in user
        return userRepository.findById(1).get(); // Replace with your logic
    }
}
