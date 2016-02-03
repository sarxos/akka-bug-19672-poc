import static akka.actor.ActorRef.noSender;
import static com.typesafe.config.ConfigFactory.parseString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ShardRegion.MessageExtractor;


public class MainSpawner {

	public static void main(String[] args) throws Exception {
		new MainSpawner().execute();
	}

	public void execute() throws Exception {

		ActorSystem system = ActorSystem.create("cluster", parseString("akka.remote.netty.tcp.port = 2552").withFallback(MainConfig.config()));
		ClusterSharding sharding = ClusterSharding.get(system);

		// wait until cluster is up

		while (Cluster.get(system).state().members().seq().size() != 2) {
			Thread.sleep(1000);
		}

		MessageExtractor extractor = new MainMessageExtractor();
		sharding.startProxy("shard", Optional.of("shard"), extractor);

		final AtomicInteger counter = new AtomicInteger(0);

		final class WatcheeActor extends UntypedActor {

			final int number;

			@SuppressWarnings("unused")
			public WatcheeActor() {
				this.number = counter.incrementAndGet();
			}

			@Override
			public void preStart() throws Exception {
				sharding.shardRegion("shard").tell(new Started(self()), self());
			}

			@Override
			public void onReceive(Object msg) throws Exception {
				if (msg instanceof Number) {
					System.out.println("stopping " + number + " " + self());
					context().stop(self());
				}
			}
		}

		System.out.println("Press any key to start actors spawning...");
		System.in.read();

		List<ActorRef> refs = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			refs.add(system.actorOf(Props.create(WatcheeActor.class, this, counter, sharding)));
		}

		Thread.sleep(5000);

		System.out.println("Press any key to start actors termination...");
		System.in.read();

		for (ActorRef ref : refs) {
			ref.tell(1, noSender());
		}

		System.out.println("Press any key to exit process...");
		System.in.read();
	}

}
