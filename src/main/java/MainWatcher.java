import static com.typesafe.config.ConfigFactory.parseString;

import java.util.function.Consumer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.cluster.sharding.ShardRegion.MessageExtractor;


public class MainWatcher {

	public static void main(String[] args) throws Exception {
		new MainWatcher().execute();
	}

	private void execute() throws Exception {

		ActorSystem system = ActorSystem.create("cluster", parseString("akka.remote.netty.tcp.port = 2551").withFallback(MainConfig.config()));
		ClusterSharding sharding = ClusterSharding.get(system);

		final class WatcherActor extends UntypedActor {

			@Override
			public void onReceive(Object msg) throws Exception {
				handle(msg).accept(msg);
			}

			private Consumer<Object> handle(Object object) {

				if (object instanceof Started) {
					return (msg) -> {
						ActorRef actor = ((Started) msg).getActor();
						System.out.println("watching " + actor);
						context().watch(actor);
					};
				}

				if (object instanceof Terminated) {
					return (msg) -> {
						ActorRef actor = ((Terminated) msg).getActor();
						System.out.println("terminated " + actor);
					};
				}

				return (msg) -> {
					unhandled(object);
				};
			}
		}

		Props props = Props.create(WatcherActor.class, this);
		ClusterShardingSettings settings = ClusterShardingSettings.create(system).withRole("shard");
		MessageExtractor extractor = new MainMessageExtractor();
		sharding.start("shard", props, settings, extractor);

		System.in.read();
	}
}
