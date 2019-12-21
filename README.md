# elasticsearch-in-action
## 全文搜索
### 数据结构
- 结构化：指具有固定格式或有限长度的数据，如数据库、元数据等。
- 非结构化：指不定长或无固定格式的数据，如邮件、word文档等。 
### 非结构化数据的检索
- 顺序扫描法（Serial Scanning）：如操作系统中文件搜索，适合小数据量
- 全文搜索（Full-text Search）：将非结构化数据的一部分转为结构的数据，然后创建索引，实现搜索的目的
### 概念
全文搜索是一种将文件中所有文本与搜索项匹配的文字资料检索方法
### 全文搜索实现原理
建文本库 》 建立索引 》 执行搜索 》 过滤结果
![全文搜索实现原理](https://img-blog.csdnimg.cn/20191221223531423.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTU0MTY1Nw==,size_16,color_FFFFFF,t_70)
### 全文搜索实现技术
#### 基于Java的开源实现
- Lucene (引擎)
- ElasticSearch ：基于Lucene，
- Solr：和ElasticSearch同类型的一个搜索技术

ElasticSearch与Solr对比

 1. Solr 利用在分布式中利用Zookeeper实现分布式管理，支持非常量多的数据格式，Json,Xml,Csv，提供非常多功能，传统应用中表现优于ElasticSearch，但在实时搜索中的效率低于ElasticSearch
 2. ElasticSearch自身就带了分布式管理系统，数据格式只支持Json，主要为了提供RestFull接口，可以使用第三方插件进行功能扩展或增强

## ElasticSearch简介
### ElasticSearch是什么？
- 高度可扩展的开源全文搜索和分析引擎
- 快速地、近实时地对大数据进行存储、搜索和分析
- 用来支撑有复杂的数据搜索需求的企业级应用
### ElasticSearch特点
- 分布式 (每个索引都使用可配置数量的分片，每个分片又可以有多个副本，在任何一个副本分片上执行读取和搜索操作)
- 高可用（正是因为分布式的特点，促成了高可用）
- 多数据类型
- 多API （Http RestFull、JavaAPI）
- 面向文档
- 异步写入（写入性能好）
- 近实时（搜索性能非常高）
- 基于Lucene搜索引擎
- Apache 开源协议
## ElasticSearch核心概念
- 近实时 
		ES每隔N秒自动做一次索引刷新，可配置
- 集群
	 	每个集群有一个唯一个集群名称
- 节点
		集群中的单一节点，节点默认使用UUID（可自定义配置节点名称）标识，在集群启动时分配，通过集群名称加入指定集群
- 索引 
		 加快搜索速度，相似文档的集合，每个索引都有一个名称，类似于Mysql中的‘库’，单位集群中，可以根据需要定义任意需要的索引
- 类型
		对索引文档中进一步的细分，类似全Mysql中的‘表’
- 文档
		是进行索引的基本单位，相当于Mysql中表的‘行’，使用Json格式表示
- 分片
		企业应用中，一般存储的数据比较大，可能会超出单个节点所能处理的范围，ES允许将索引分成多个分片来存储这个索引的部分数据，ES会负责这个分片的分配和聚合，对于数据的可靠性，ES还会对这个分片建立副本，这样ES每个索引就会分配多个分片和多个副本，ES自动管理这些节点中的分片和副本。设置分片主要原因是一方面进行水平的分隔，其次，数据分配在多个节点中可以并行查询，提高性能和吞吐量
-	副本
		第一：提供高可用
		第二：提高搜索量和吞吐量
		
默认情况下，ES每个索引会分配5个分片，每个分片有1个副本。相当于一个集群10个分片

## Elasticsearch 与 Spring Boot集成
### 配置环境
- Spring Boot 2.2.2.RELEASE
- Elasticsearch 6.1.1
	https://www.elastic.co/cn/downloads/past-releases
- Spring Data Elasticsearch 2.2.2.RELEASE
- JNA 4.3.0 (Elasticsearch 需要此依赖)

## ElasticSearch实战
### pom.xml
```
	 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>


        <dependency>
            <groupId>net.java.dev.jna</groupId>
            <artifactId>jna</artifactId>
             <version>4.3.0</version>
        </dependency>
```

### application.properties

```
#Elasticsearch服务地址
spring.data.elasticsearch.cluster-nodes=localhost:9300
#设置连接超时时间
spring.data.elasticsearch.properties.transport.tcp.connect_timeout=120s
```

### EsBlog.Java

```
package com.example.elasticsearchinaction.blog.domain.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;



/**
 * Created by Axin in 2019/12/20 22:11
 */

@Document(indexName = "blog", type="blog")
public class EsBlog implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容
     */
    private String content;

    /*
     *protected 修饰 ，JPA规范要求；防止直接使用
     */
    protected EsBlog() {

    }

    public EsBlog(String title, String summary, String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EsBlog{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

```
### EsBlogRepository.java 接口

```
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

```

### 编写测试代码 EsBlogRepositoryTest.java

```
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
        //断言
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

```
### 启动本地ElasticSearch服务
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191221225750120.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTU0MTY1Nw==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191221225759736.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTU0MTY1Nw==,size_16,color_FFFFFF,t_70)
### 运行测试代码，打印结果 

```
============================
EsBlog{id='GSTyKG8BGy-dUfJgRiB3', title='关山月', summary='李白的关山月', content='明月出天山，苍茫云海间。
长风几万里，吹度玉门关。'}
EsBlog{id='GiTyKG8BGy-dUfJgRyAV', title='望月怀远', summary='张九龄的望月怀远', content='海上生明月，天涯共此时。
情人怨遥夜，竟夕起相思。'}
EsBlog{id='GyTyKG8BGy-dUfJgRyCz', title='静夜思', summary='李白《静夜思》', content='举头望明月,低头思故乡'}
============================
```

### BlogController.java

```
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

```
###  启动项目
运行 ElasticsearchInActionApplication.java

### 浏览器访问
浏览器地址输入：http://localhost:8080/blog/list?title=月&summary=月&content=月

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191221230121902.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTU0MTY1Nw==,size_16,color_FFFFFF,t_70)



> <img src="https://img-blog.csdnimg.cn/20191221230544118.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTU0MTY1Nw==,size_16,color_FFFFFF,t_70" width = "50" height = "50" div align=left/> [https://github.com/gitAxin/elasticsearch-in-action.git](https://github.com/gitAxin/elasticsearch-in-action.git)
