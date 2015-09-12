import org.junit.*;
import static org.junit.Assert.*;

public class ClientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(0, Client.all().size());
  }

  @Test
  public void getName_returnsName() {
    Client myClient = new Client("Rowdy", 1);
    assertEquals("Rowdy", myClient.getName());
  }

  @Test
  public void getId_returnsIdAfterSave() {
    Client instance = new Client("Rowdy", 1);
    instance.save();
    assertEquals(Client.all().get(0).getId(), instance.getId());
  }

  @Test
  public void equals_returnsTrueWhenParamsMatch() {
    Client firstInstance = new Client("Rowdy", 1);
    Client secondInstance = new Client("Rowdy", 1);
    assertEquals(true, firstInstance.equals(secondInstance));
  }

  @Test
  public void equals_returnsFalseWhenParamsAreDifferent() {
    Client firstInstance = new Client("Rowdy",  1);
    Client secondInstance = new Client("Princess", 2);
    assertEquals(false, firstInstance.equals(secondInstance));
  }

  @Test
  public void save_addsToDatabase() {
    Client instance = new Client("Rowdy", 1);
    instance.save();
    assertEquals(Client.all().get(0), instance);
  }

  @Test
  public void find_findsClientInDatabase_true() {
    Client myClient = new Client("Rowdy", 1);
    myClient.save();
    Client savedClient = Client.find(myClient.getId());
    assertTrue(myClient.equals(savedClient));
  }

  @Test
  public void updateName_changesNameInDatabase_true() {
    Client myClient = new Client("Rowdy", 1);
    myClient.save();
    String newName = "Kujo";
    myClient.updateName(newName);
    assertEquals(newName, Client.all().get(0).getName());
  }


  @Test
  public void delete_FromDatabase_true() {
    Client myClient = new Client("Rowdy", 1);
    myClient.save();
    myClient.delete();
    assertEquals(0, Client.all().size());
  }
}
