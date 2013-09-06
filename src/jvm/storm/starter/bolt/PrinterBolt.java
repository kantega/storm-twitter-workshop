package storm.starter.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.apache.log4j.Logger;


public class PrinterBolt extends BaseBasicBolt {

    private static final Logger LOG = Logger.getLogger(IntermediateRankingsBolt.class);

    @Override
  public void execute(Tuple tuple, BasicOutputCollector collector) {
    LOG.info("RANKING: "+tuple);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer ofd) {
  }

}
