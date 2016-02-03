import java.io.Serializable;

import akka.actor.ActorRef;


public class Started implements Serializable {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	private final ActorRef actor;

	public Started(ActorRef actor) {
		this.actor = actor;
	}

	public ActorRef getActor() {
		return actor;
	}
}
