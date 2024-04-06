package org.shaotang.distribution.example.es.client;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.WeightBuilder;
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
import java.util.Map;
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

       BoolQueryBuilder keyWordBool = QueryBuilders.boolQuery();
       //            keyWordBool.should(new WildcardQueryBuilder("code", "*" + keyword + "*").boost(100.0f));
        // 加权重，不知道有没有用
        String keyword="轧";
        WildcardQueryBuilder code = new WildcardQueryBuilder("code", "*" + keyword + "*");
        WeightBuilder weightBuilder = ScoreFunctionBuilders.weightFactorFunction(1000);
        keyWordBool.should(QueryBuilders.functionScoreQuery(code, weightBuilder).boost(1000.0f));
        keyWordBool.should(QueryBuilders.multiMatchQuery(keyword,  fields));
        bool.must(keyWordBool);


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

        // 获取高亮结果
        List<SynonymDocument> results = searchHits.getSearchHits().stream()
                .map(searchHit -> {
                    SynonymDocument doc = searchHit.getContent();
                    Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
                    if (highlightFields.containsKey("name")) {
                        doc.setName((highlightFields.get("name").get(0)));
                    }
                    return doc;
                })
                .collect(Collectors.toList());

        System.out.println(list);
        System.out.println(results);

    }

    public static void main(String[] args) {
        new TestSynonym().esDynamicSynonymSearch();
        System.out.println();
    }
}
