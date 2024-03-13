package org.shaotang.distribution.example.es.client;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TestSynonym {

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    public void esDynamicSynonymSearch() {

        String indexName = "synonym-material-dic-item-doc-log";
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        List<String> fieldList = Arrays.asList("name", "specification");
        String[] fields = fieldList.toArray(new String[]{});
        bool.must(QueryBuilders.multiMatchQuery("轧", fields));
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<font style='color:red'>");
        hiBuilder.postTags("</font>");
        hiBuilder.highlighterType("unified");
        for (String field : fieldList) {
            hiBuilder.field(field, 100, 5);
        }
        hiBuilder.requireFieldMatch(false);//多次段高亮需要设置为false
        query.withHighlightBuilder(hiBuilder);
        query.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        query.withQuery(bool);
        log.info("bool:" + bool);
        SearchHits<SynonymDocument> searchHits = restTemplate.search(query.build(), SynonymDocument.class, indexCoordinates);
        //        if (CollectionUtils.isEmpty(documents)) {
////            throw new MorrowRequestException(ErrorCodes.ES_QUERY_DATA_IS_NULL);
//            return documents;
//        }
        List<SynonymDocument> list = searchHits.get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        System.out.println(list);
    }

    public static void main(String[] args) {
        new TestSynonym().esDynamicSynonymSearch();
        System.out.println();
    }
}
