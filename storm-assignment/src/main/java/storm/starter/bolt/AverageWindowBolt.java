package storm.starter.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.log4j.Logger;

import java.util.Iterator;


/**
 * Bolt calculating a running average value.
 */
public class AverageWindowBolt extends BaseBasicBolt {

    private static final Logger LOG = Logger.getLogger(AverageWindowBolt.class);

    double avgValue = 0d;
    private String field;
    private CircularFifoBuffer fifo;

    /**
     * Constructor
     * @param field The tuple field from which the average value should be calculated
     * @param windowSize Limit for how many items the average value should be based on.
     */
    public AverageWindowBolt(String field, int windowSize){
        this.field = field;
        this.fifo = new CircularFifoBuffer(windowSize);
    }


    @Override
  public void execute(Tuple tuple, BasicOutputCollector collector) {
        fifo.add(tuple);
        updateAvgValue();
        collector.emit(new Values(avgValue));
  }

    private void updateAvgValue() {
        double sum = 0;
        int n = 0;

        Iterator iter = fifo.iterator();

        while(iter.hasNext()){
            Tuple tuple = (Tuple) iter.next();
            sum += tuple.getDoubleByField(field);
            n++;
        }
        avgValue = 0;
        if (n != 0)
            avgValue = sum / n;

    }

    @Override
  public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("avg-value"));
  }

}
