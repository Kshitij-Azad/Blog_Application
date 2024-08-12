package com.pro.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.pro.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // Additional query methods if needed
	@Query("SELECT p FROM Post p")
	 List<Post> getAllPosts();
	
	 List<Post> findByUserEmail(String email);

}
