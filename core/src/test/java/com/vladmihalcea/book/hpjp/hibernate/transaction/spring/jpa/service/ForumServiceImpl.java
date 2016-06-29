package com.vladmihalcea.book.hpjp.hibernate.transaction.spring.jpa.service;

import com.vladmihalcea.book.hpjp.hibernate.transaction.forum.Post;
import com.vladmihalcea.book.hpjp.hibernate.transaction.spring.jpa.dao.PostDAO;
import com.vladmihalcea.book.hpjp.hibernate.transaction.spring.jpa.dao.TagDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <code>ForumServiceImpl</code> - Forum Service Impl
 *
 * @author Vlad Mihalcea
 */
@Repository
public class ForumServiceImpl implements ForumService {

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private TagDAO tagDAO;

    @Override
    @Transactional
    public Post newPost(String title, String... tags) {
        Post post = new Post();
        post.setTitle(title);
        post.getTags().addAll(tagDAO.findByName(tags));
        return postDAO.persist(post);
    }
}
