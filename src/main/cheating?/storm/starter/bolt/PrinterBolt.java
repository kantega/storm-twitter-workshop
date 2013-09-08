package storm.starter.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.apache.log4j.Logger;


public class PrinterBolt extends BaseBasicBolt {

    private String prefix;

    public PrinterBolt(String prefix){
        this.prefix = prefix;
    }


    @Override
  public void execute(Tuple tuple, BasicOutputCollector collector) {
    System.err.println(prefix+": "+tuple);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer ofd) {
  }

}
