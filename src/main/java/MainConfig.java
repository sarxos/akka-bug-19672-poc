import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class MainConfig {

	public static Config config() {

		return ConfigFactory.parseString("" +
			"akka {\n" +
			"  actor {\n" +
			"    provider = \"akka.cluster.ClusterActorRefProvider\"\n" +
			"  }\n" +
			"  remote {\n" +
			"    netty.tcp {\n" +
			"      hostname = \"127.0.0.1\"\n" +
			"     }\n" +
			"  }\n" +
			"  cluster {\n" +
			"    seed-nodes = [\"akka.tcp://cluster@127.0.0.1:2551\"]\n" +
			"    metrics.enabled = off\n" +
			"    roles = [ shard ]\n" +
			"  }\n" +
			"  persistence {\n" +
			"    journal.plugin = \"inmemory-journal\"\n" +
			"    snapshot-store.plugin = \"inmemory-snapshot-store\"\n" +
			"  }\n" +
			"}");
	}

}
