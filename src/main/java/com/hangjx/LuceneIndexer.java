package com.hangjx;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Dell on 2021/7/4.
 */

 public class LuceneIndexer {
    private volatile static LuceneIndexer instance;

    private final static String INDEX_DIR = "D:\\lucene";

    private static class SingletonHolder{
        private final static LuceneIndexer instance=new LuceneIndexer();
    }

    public static LuceneIndexer getInstance(){
        return SingletonHolder.instance;
    }

    public boolean createIndex(String indexDir) throws IOException{
        //加点测试的静态数据
        Integer ids[] = {1 , 2 , 3};
        String titles[] = {"标题1" , "标题2" , "标题3"};
        String tcontents[] = {
                "内容1内容啊哈哈哈",
                "内容2内容啊哈哈哈",
                "内容3内容啊哈哈哈"
        };

        long startTime = System.currentTimeMillis();//记录索引开始时间

        Analyzer analyzer = new SmartChineseAnalyzer();
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, config);

        for(int i = 0; i < ids.length;i++){
            Document doc = new Document();
            //添加字段
            doc.add(new IntField("id", ids[i],Field.Store.YES)); //添加内容
            doc.add(new TextField("title", titles[i], Field.Store.YES)); //添加文件名，并把这个字段存到索引文件里
            doc.add(new TextField("tcontent", tcontents[i], Field.Store.YES)); //添加文件路径
            indexWriter.addDocument(doc);

        }

        indexWriter.commit();
        System.out.println("共索引了"+indexWriter.numDocs()+"个文件");
        indexWriter.close();
        System.out.println("创建索引所用时间："+(System.currentTimeMillis()-startTime)+"毫秒");

        return true;
    }

    public static void main(String[] args) {
        try {
            boolean r = LuceneIndexer.getInstance().createIndex(INDEX_DIR);
            if(r){
                System.out.println("索引创建成功!");
            }else{
                System.out.println("索引创建失败!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


