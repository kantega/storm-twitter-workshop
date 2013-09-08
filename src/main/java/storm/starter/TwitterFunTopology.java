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
        // TODO: Define your own twitter query
        //tweetFilterQuery.track(new String[]{"#valg13", "#valg2013", "#nyregjering"});

        TwitterSpout spout = new TwitterSpout(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET, tweetFilterQuery);

        //TODO: Set the twitter spout as spout on this topology. Hint: Use the builder object.

        PrinterBolt printerBolt = new PrinterBolt("TWEET! ");
        //TODO: Route messages from the spout to the printer bolt. Hint: Again, use the builder object.


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
