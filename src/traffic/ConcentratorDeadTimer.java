package traffic;

/**
 * Waits for LoRa messages to synchronize
 * @author Karol Cagáň
 * @version 0.3
 */
public class ConcentratorDeadTimer extends Thread {
  private final String key;
  private final LoRaConcentrator parent;
  private final boolean isRegistration;
  private final boolean isEmergency;
  private final int sleepTime;

  /**
   * Constructor
   * @param key hash table key
   * @param parent lora concentrator
   * @param type message type
   */
  public ConcentratorDeadTimer(String key, LoRaConcentrator parent, String type) {
    this.key = key;
    this.parent = parent;
    this.isRegistration = type.equals("reg");
    this.isEmergency = type.equals("emer");
    this.sleepTime = parent.programResources.props.getInt("ConcentratorDeadTimer.waitTime");
    // this.run();
  }

  public void run() {
    try {
      // Time to wait for all messages to come, e.g. synchronization timer
      // No need to wait in case of emergency
      if (!this.isEmergency) {
        System.out.println("Waiting " + this.sleepTime + " ms for other messages");
        sleep(this.sleepTime);
      } else {
        System.out.println("Emergency message received, skipping message synchronization");
      }

      if (!isRegistration) {
        parent.finalize(key);
      } else {
        parent.register(key);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
