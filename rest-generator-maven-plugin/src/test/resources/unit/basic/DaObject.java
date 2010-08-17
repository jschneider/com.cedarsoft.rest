package unit.basic;

/**
 * Thist must be in test/resources to be available as source code for the unit test
 */
public class DaObject {
  private final OtherObject object;

  public DaObject( OtherObject object ) {
    this.object = object;
  }

  public OtherObject getObject() {
    return object;
  }
}
