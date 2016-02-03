import akka.cluster.sharding.ShardRegion.MessageExtractor;


public class MainMessageExtractor implements MessageExtractor {

	@Override
	public String entityId(Object msg) {
		return "1";
	}

	@Override
	public Object entityMessage(Object msg) {
		return msg;
	}

	@Override
	public String shardId(Object msg) {
		return "1";
	}
}
