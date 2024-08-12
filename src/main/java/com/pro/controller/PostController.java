package com.pro.controller;



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.pro.entity.Comment;
import com.pro.entity.Post;
import com.pro.entity.User;
import com.pro.helper.Message;
import com.pro.repo.CommentRepository;
import com.pro.repo.PostRepository;
import com.pro.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class PostController {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	
	@RequestMapping("/home")
	public String home(Model m,Principal principal,User user)
	{
		try {
			List<Post> postList = this.postRepository.getAllPosts();
			m.addAttribute("postList", postList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/home";
	}
	
	@RequestMapping("/about")
	public ModelAndView about()
	{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/about");
		return modelAndView;
	}
	@RequestMapping("/mypost")
	public String MyPost (Model m,Principal principal)
	{
		try {
	        // Fetch the current user's email
	        String email = principal.getName();
	        
	        // Fetch posts associated with the user's email
	        List<Post> postList = this.postRepository.findByUserEmail(email);
	        
	        // Add the posts to the model
	        m.addAttribute("postList", postList);
	        
	    } catch (Exception e) {
	        // Handle the exception
	        e.printStackTrace(); // Or use a logging framework
	    }
	    return "user/mypost";
	}
	
	
	
//	{
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("user/mypost");
//		return modelAndView;
//	}
	@RequestMapping("/createpost")
	public ModelAndView createpost()
	{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/createpost");
		return modelAndView;
	}
	
	@PostMapping("/savePost")
	public String processProduct(@ModelAttribute Post post,
//			@RequestParam("img") MultipartFile file, 
			Principal principal, HttpSession session) {
	    try {
	        String username = principal.getName();
	        User user = this.userRepository.getUserByUserName(username);
	        user.getPosts().add(post);
	        post.setUser(user);

	        // Processing and uploading file
//	        if (!file.isEmpty()) {
//	            // Save the file
//	            String filename = file.getOriginalFilename();
//	            String uploadDir = "static/image/";
//	            
//	            // Ensure directory exists
//	            File uploadDirFile = new File(uploadDir);
//	            if (!uploadDirFile.exists()) {
//	                uploadDirFile.mkdirs();
//	            }
//
//	            // Save the file
//	            Path path = Paths.get(uploadDir + filename);
//	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//	            post.setImg(filename);
//	        }

	        // Save the user
	        this.userRepository.save(user);
	        System.out.println("Data " + post);
	        System.out.println("Added to Database");
	        session.setAttribute("message", new Message("Done" , "danger"));     
	        // Success message
	        return "redirect:/user/home";
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Failure message
	        session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "danger"));
	    }
	    return "redirect:/user/create";
	}

	@RequestMapping("/viewMyPost")
	public ModelAndView View()
	{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/viewMyPost");
		return modelAndView;
	}
	@RequestMapping("/viewPost")
	public ModelAndView ViewPost()
	{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/viewPost");
		return modelAndView;
	}
	
	@RequestMapping("/editPost")
	public ModelAndView editPostt()
	{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/editPost");
		return modelAndView;
	}
	
	@RequestMapping("/updatePost")
	public String modifyProduct(Model m,Principal principal)
	{
		try {
			List<Post> postList = this.postRepository.getAllPosts();
			m.addAttribute("postList", postList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "admin/viewMyPost";
	}
	@PostMapping("/updatePostprocess")
	public String updateFormProductProcess(@ModelAttribute Post post)
	{
		 Post existingPost = this.postRepository.findById(post.getPid())
                 .orElseThrow(() -> new IllegalArgumentException("Invalid product Id"));
		 existingPost.setTitle(post.getTitle());
	     existingPost.setContent(post.getContent());
		this.postRepository.save(existingPost);
		return"redirect:/user/mypost";
	}
	
	@GetMapping("/delete1/{pid}")
	public String deleteProduct(@PathVariable("pid")Integer pid,Model model,HttpSession httpSession)
	{
		Optional<Post> postId = this.postRepository.findById(pid);
		Post post = postId.get();
		this.postRepository.delete(post);
		httpSession.setAttribute("message", new Message("Delete Product Successfully","success"));		
		
		return "redirect:/user/mypost";
	}
	@GetMapping("/editPost/{pid}")
	public String updateFormProduct(@PathVariable("pid")Integer pid,Model model)
	{
		model.addAttribute("title","Update Post");
		Post post = this.postRepository.findById(pid).get();
		
		model.addAttribute("post", post);
		return "user/editPost";
	}
	
	@GetMapping("/viewPost/{pid}")
	public String viewPostById(@PathVariable("pid") Integer pid, Model model, HttpSession session) {
	    try {
	        Optional<Post> optionalPost = this.postRepository.findById(pid);
	        if (optionalPost.isPresent()) {
	            Post post = optionalPost.get();
//	            List<Comment> comments = commentRepository.findByPost(post);
	            List<Comment> commentList = commentRepository.findByPost(post);
    			model.addAttribute("commentList", commentList);
	            try {
	            	
	    		} catch (Exception e) {
	    			// TODO: handle exception
	    		}
	            model.addAttribute("post", post);
	            return "user/viewPost";
	        } else {
	            session.setAttribute("message", new Message("Post not found!", "danger"));
	            return "redirect:/user/home";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "danger"));
	        return "redirect:/user/home";
	    }
	}
	
	

	
}
