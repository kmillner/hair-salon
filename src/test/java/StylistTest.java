import org.junit.*;
import static org.junit.Assert.*;

public class StylistTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(0, Stylist.all().size());
  }

  @Test
  public void getName_returnsName() {
    Stylist myStylist = new Stylist( "Teresa","Coloring");
    assertEquals("Teresa", myStylist.getName());
  }

  @Test
  public void getId_returnsIdAfterSave() {
    Stylist instance = new Stylist("Teresa","Coloring");
    instance.save();
    assertEquals(Stylist.all().get(0).getId(), instance.getId());
  }

  @Test
  public void equals_returnsTrueWhenParamsMatch() {
    Stylist firstInstance = new Stylist("Teresa","Coloring");
    Stylist secondInstance = new Stylist("Teresa","Coloring");
    assertEquals(true, firstInstance.equals(secondInstance));
  }

  @Test
  public void equals_returnsFalseWhenParamsAreDifferent() {
    Stylist firstInstance = new Stylist("Teresa","Coloring");
    Stylist secondInstance = new Stylist("Jason", "Shaving");
    assertEquals(false, firstInstance.equals(secondInstance));
  }

  @Test
  public void save_addsToDatabase() {
    Stylist instance = new Stylist("Teresa","Coloring");
    instance.save();
    assertEquals(Stylist.all().get(0), instance);
  }

  @Test
  public void find_findsStylistInDatabase_true() {
    Stylist myStylist = new Stylist("Teresa","Coloring");
    myStylist.save();
    Stylist savedStylist = Stylist.find(myStylist.getId());
    assertTrue(myStylist.equals(savedStylist));
  }

  @Test
  public void updateName_changesNameInDatabase_true() {
    Stylist myStylist = new Stylist("Teresa","Coloring");
    myStylist.save();
    String newName = "Riza";
    myStylist.updateName(newName);
    assertEquals(newName, Stylist.all().get(0).getName());
  }

  @Test
  public void updateService_changesServiceInDatabase_true() {
    Stylist myStylist = new Stylist("Teresa","Coloring");
    myStylist.save();
    String newService = "Nail Trimming";
    myStylist.updateService(newService);
    assertEquals(newService, Stylist.all().get(0).getService());
  }

  @Test
  public void delete_FromDatabase_true() {
    Stylist myStylist = new Stylist("Teresa","Coloring");
    myStylist.save();
    myStylist.delete();
    assertEquals(0, Stylist.all().size());
  }

}
