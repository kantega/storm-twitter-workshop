import backtype.storm.LocalCluster;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import backtype.storm.Config;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TwitterSpout fetches messages from Twitter Streaming API using Twitter4j.
 * Created with IntelliJ IDEA.
 * User: qadeer
 * Date: 05.09.13
 * Time: 22:50
 */
public class TwitterSpout extends BaseRichSpout {

    public static final String MESSAGE = "msg";
    private final String _accessTokenSecret;
    private final String _accessToken;
    private final String _consumerSecret;
    private final String _consumerKey;
    private SpoutOutputCollector _collector;
    private TwitterStream _twitterStream;
    private LinkedBlockingQueue _msgs;
    private FilterQuery _tweetFilterQuery;


    public TwitterSpout(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        if (consumerKey == null ||
                consumerSecret == null ||
                accessToken == null ||
                accessTokenSecret == null) {
            throw new RuntimeException("Twitter4j OAuth field cannot be null");
        }

        _consumerKey = consumerKey;
        _consumerSecret = consumerSecret;
        _accessToken = accessToken;
        _accessTokenSecret = accessTokenSecret;


    }

    public TwitterSpout(String arg, String arg1, String arg2, String arg3, FilterQuery filterQuery) {
        this(arg,arg1,arg2,arg3);
        _tweetFilterQuery = filterQuery;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(MESSAGE));
    }

    /**
     * Creates a twitter stream listener which adds messages to a LinkedBlockingQueue.
     */
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        _msgs = new LinkedBlockingQueue();
        _collector = spoutOutputCollector;
        ConfigurationBuilder _configurationBuilder = new ConfigurationBuilder();
        _configurationBuilder.setOAuthConsumerKey(_consumerKey)
                .setOAuthConsumerSecret(_consumerSecret)
                .setOAuthAccessToken(_accessToken)
                .setOAuthAccessTokenSecret(_accessTokenSecret);
        _twitterStream = new TwitterStreamFactory(_configurationBuilder.build()).getInstance();
        _twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if (meetsConditions(status))
                    _msgs.offer(status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onException(Exception ex) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        _twitterStream.filter(_tweetFilterQuery);


    }

    private boolean meetsConditions(Status status) {
        return true;
    }

    /**
     * When requested for next tuple, reads message from queue and emits the message.
     */
    @Override
    public void nextTuple() {
        // emit tweets
        Object s = _msgs.poll();
        if (s == null) {
            Utils.sleep(1000);
        } else {
            _collector.emit(new Values(s));
        }
    }

    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();
        FilterQuery filterQuery = new FilterQuery();
        // Filter close to Norway
        filterQuery.locations(new double[][]{new double[]{3.339844, 53.644638},
                new double[]{18.984375,72.395706
                }});

        TwitterSpout spout = new TwitterSpout(args[0], args[1], args[2], args[3], filterQuery);

        builder.setSpout("twitterSpout", spout, 1);
        builder.setBolt("fileWriter", new FileWriterBolt(),1).shuffleGrouping("twitterSpout");


        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(2);
        cluster.submitTopology("test", conf, builder.createTopology());
        // Keep going for milliseconds
        Utils.sleep(10000);
        cluster.shutdown();

    }

}
