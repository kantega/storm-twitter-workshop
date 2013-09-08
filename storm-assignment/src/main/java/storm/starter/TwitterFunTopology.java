package storm.starter;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import storm.starter.bolt.PrinterBolt;
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

    private static String consumerKey = "FILL IN HERE";
    private static String consumerSecret = "FILL IN HERE";
    private static String accessToken = "FILL IN HERE";
    private static String accessTokenKey = "FILL IN HERE";


    public static void main(String[] args) throws Exception {
        /**************** SETUP ****************/
        String remoteClusterTopologyName = null;
        if (args!=null) {
            if (args.length==1) {
                remoteClusterTopologyName = args[0];
            }
            // If credentials are provided as commandline arguments
            else if (args.length==4) {
                consumerKey =args[0];
                consumerSecret =args[1];
                accessToken =args[2];
                accessTokenKey =args[3];
            }

        }
        /****************       ****************/

        TopologyBuilder builder = new TopologyBuilder();

        FilterQuery tweetFilterQuery = new FilterQuery();
        // TODO: Define your own twitter query
        //tweetFilterQuery.track(new String[]{"#valg13", "#valg2013", "#nyregjering"});


        TwitterSpout spout = new TwitterSpout(consumerKey, consumerSecret, accessToken, accessTokenKey, tweetFilterQuery);

        //TODO: Set the twitter spout as spout on this topology. Hint: Use the builder object.

        PrinterBolt printerBolt = new PrinterBolt("TWEET! ");
        //TODO: Route messages from the spout to the printer bolt. Hint: Again, use the builder object.


        Config conf = new Config();
        conf.setDebug(true);


        if (remoteClusterTopologyName!=null) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(remoteClusterTopologyName, conf, builder.createTopology());
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
