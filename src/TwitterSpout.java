import backtype.storm.LocalCluster;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import twitter4j.RawStreamListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import backtype.storm.Config;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: qadeer
 * Date: 05.09.13
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class TwitterSpout extends BaseRichSpout {
    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    private static final String ACCESS_TOKEN = "";
    private static final String ACCESS_SECRET = "";
    private String _username;
    private String _pwd;
    private SpoutOutputCollector _collector;
    private TwitterStream _twitterStream;
    private LinkedBlockingQueue _msgs;

    public TwitterSpout(String username, String pwd) {

        _username = username;
        _pwd = pwd;


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("msg"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        _msgs = new LinkedBlockingQueue();
        _collector = spoutOutputCollector;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_SECRET);
        _twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        _twitterStream.addListener(new RawStreamListener() {
            @Override
            public void onMessage(String rawString) {
                _msgs.offer(rawString);
            }

            @Override
            public void onException(Exception ex) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        _twitterStream.sample();


        // connect to twitter
    }

    @Override
    public void nextTuple() {
        // emit tweets
        Object s = _msgs.poll();
        if (s==null){
            Utils.sleep(1000);
        }
        else {
            _collector.emit(new Values(s));
        }
    }

    public static void main(String[] args){
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("twitterSpout", new TwitterSpout(null,null),1);
        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(2);
        cluster.submitTopology("test",conf,builder.createTopology());

    }

}
