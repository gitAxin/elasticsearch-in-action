package com.example.elasticsearchinaction.blog.controller;

import com.example.elasticsearchinaction.blog.domain.es.EsBlog;
import com.example.elasticsearchinaction.blog.repository.es.EsBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Axin in 2019/12/20 23:26
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private EsBlogRepository repository;


    @RequestMapping("/list")
    public List<EsBlog> list (@RequestParam(value="title") String title,
                              @RequestParam(value="summary") String summary,
                              @RequestParam(value="content") String content,
                              @RequestParam(value="pageIndex",defaultValue="0") int pageIndex,
                              @RequestParam(value="pageSize",defaultValue="10") int pageSize){

        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        Page<EsBlog> page = repository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title, summary, content,pageable);
        return page.getContent();

    }
}
