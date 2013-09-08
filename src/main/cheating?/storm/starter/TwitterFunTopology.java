package storm.starter;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import storm.starter.bolt.*;
import storm.starter.spout.TwitterSpout;
import twitter4j.FilterQuery;

/**
 * Created with IntelliJ IDEA.
 * User: joning
 * Date: 06.09.13
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class TwitterFunTopology {

    private static final String CONSUMER_KEY="<FILL ME INN>";
    private static final String CONSUMER_SECRET="<FILL ME INN>";
    private static final String ACCESS_TOKEN="<FILL ME INN>";
    private static final String ACCESS_TOKEN_SECRET="<FILL ME INN>";


    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        FilterQuery tweetFilterQuery = new FilterQuery();
        // Filter close to Norway
        tweetFilterQuery.track(new String[]{"#valg13", "#valg2013", "#nyregjering"});
        builder.setSpout("spout", new TwitterSpout(CONSUMER_KEY,CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET,tweetFilterQuery), 1);
        builder.setBolt("language-detection", new LanguageDetectionBolt(), 4).shuffleGrouping("spout");

        builder.setBolt("sentiment", new SentimentBolt(), 4).shuffleGrouping("language-detection");
        builder.setBolt("avg-sentiment", new AverageWindowBolt("sentiment-value"), 4).shuffleGrouping("sentiment");
        builder.setBolt("avg-sentiment-print", new PrinterBolt("AVG SENTIMENT")).shuffleGrouping("avg-sentiment");

        builder.setBolt("hashtags", new HashtagExtractionBolt(), 4).shuffleGrouping("sentiment");
        builder.setBolt("hashtag-counter", new RollingCountBolt(9, 3), 4).fieldsGrouping("hashtags", new Fields("entity"));
        builder.setBolt("hashtag-intermediate-ranking", new IntermediateRankingsBolt(100), 4).fieldsGrouping("hashtag-counter", new Fields(
                "obj"));
        builder.setBolt("hashtag-total-ranking", new TotalRankingsBolt(100)).globalGrouping("hashtag-intermediate-ranking");
        builder.setBolt("hashtag-ranking-print", new PrinterBolt("HASHTAG_RANKING")).shuffleGrouping("hashtag-total-ranking");


 /*       builder.setBolt("feeds", new FeedEntityExtractionBolt(), 4).shuffleGrouping("spout");
        builder.setBolt("feed-counter", new RollingCountBolt(9, 3), 4).fieldsGrouping("feeds", new Fields("entity"));
        builder.setBolt("feed-intermediate-ranking", new IntermediateRankingsBolt(100), 4).fieldsGrouping("feed-counter", new Fields(
                "obj"));
        builder.setBolt("feed-total-ranking", new TotalRankingsBolt(100)).globalGrouping("feed-intermediate-ranking");
   */



        Config conf = new Config();
        conf.setDebug(true);


        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
        else {
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("twitter-fun", conf, builder.createTopology());

            Thread.sleep(460000);

            cluster.shutdown();
        }
    }
}
