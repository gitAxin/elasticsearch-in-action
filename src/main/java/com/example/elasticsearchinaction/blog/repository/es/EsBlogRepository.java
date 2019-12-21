package com.example.elasticsearchinaction.blog.repository.es;

import com.example.elasticsearchinaction.blog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by Axin in 2019/12/20 22:50
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog,String> {


    /**
     * 分页查询博客（去重）
     * @param title
     * @param summary
     * @param content
     * @return
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(String title, String summary, String content, Pageable pageable);

}
