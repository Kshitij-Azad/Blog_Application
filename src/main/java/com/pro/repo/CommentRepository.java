package com.pro.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pro.entity.Comment;
import com.pro.entity.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("SELECT p FROM Comment p")
	 List<Comment> getAllComment();
	
	List<Comment> findByPost(Post post);
}
