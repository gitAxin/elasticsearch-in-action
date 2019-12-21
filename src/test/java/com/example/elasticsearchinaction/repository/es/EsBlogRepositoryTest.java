package com.example.elasticsearchinaction.repository.es;


import com.example.elasticsearchinaction.blog.domain.es.EsBlog;
import com.example.elasticsearchinaction.blog.repository.es.EsBlogRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * Created by Axin in 2019/12/20 22:50
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsBlogRepositoryTest {


    @Autowired
    private EsBlogRepository esBlogrepository;


    @Before
    public void initRRepositoryData(){
        //清除所有数据
        esBlogrepository.deleteAll();
        esBlogrepository.save(new EsBlog("关山月","李白的关山月","明月出天山，苍茫云海间。\n" +
                "长风几万里，吹度玉门关。"));
        esBlogrepository.save(new EsBlog("望月怀远","张九龄的望月怀远","海上生明月，天涯共此时。\n" +
                "情人怨遥夜，竟夕起相思。"));
        esBlogrepository.save(new EsBlog("静夜思","李白《静夜思》","举头望明月,低头思故乡"));

    }
    /**
     * 分页查询博客（去重）
     *
     */
    @Test
   public void testFindDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(){
        Pageable pageable = PageRequest.of(0,20);

        String title = "月";
        String summary = "月";
        String content = "月";
        Page<EsBlog> page = this.esBlogrepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title, summary, content,pageable);
        int numberOfElements = page.getNumberOfElements();
        Assert.assertEquals(3,numberOfElements);
        page.get();

        System.out.println("============================");
        List<EsBlog> content1 = page.getContent();
        for (EsBlog esBlog : content1) {
            System.out.println(esBlog);
        }
        System.out.println("============================");


    }

}
