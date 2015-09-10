import org.sql2o.*;
import java.util.List;

public class Client {

  private int id;
  private String name;
  private int stylist_id;


  public Client (String name, int stylist_id) {
    this.name = name;
    this.stylist_id = stylist_id;
  }

  public String getName() {
    return name;
  }

  public int getStylistId() {
    return stylist_id;
  }
  
  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherClientInstance) {
    if (!(otherClientInstance instanceof Client)) {
      return false;
    } else {
      Client newClientInstance = (Client) otherClientInstance;
      return this.getName().equals(newClientInstance.getName()) &&
             this.getStylistId() == newClientInstance.getStylistId() &&
             this.getId() == newClientInstance.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Clients (name, stylist_id) VALUES (:name, :stylist_id);";
      this.id = (int) con.createQuery(sql, true)
          .addParameter("name", name)
          .addParameter("stylist_id", stylist_id)
          .executeUpdate()
          .getKey();
    }
  }

  public void updateName(String name) {
    this.name = name;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE Clients SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateStylist_id(int stylist_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET stylist_id = :stylist_id WHERE id = :id";
      con.createQuery(sql)
        .addParameter("stylist_id", stylist_id)
        .addParameter("id", id)
        .executeUpdate();
    }
  }  

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM Clients WHERE id = :id";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();
      String joinDeleteQuery = "DELETE FROM Clients_Stylists WHERE Client_id = :id";
        con.createQuery(joinDeleteQuery)
          .addParameter("id", id)
          .executeUpdate();
    }
  }

  public static List<Client> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Clients";
      return con.createQuery(sql).executeAndFetch(Client.class);
    }
  }

  public static Client find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Clients WHERE id=:id";
      Client Client = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Client.class);
      return Client;
    }
  }

  public void addStylist(Stylist stylist) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Clients_Stylists (Client_id, Stylist_id) VALUES (:Client_id, :Stylist_id)";
      con.createQuery(sql)
        .addParameter("Client_id", id)
        .addParameter("Stylist_id", Stylist.getId())
        .executeUpdate();
    }
  }

  public List<Stylist> getStylists() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT Stylists.* FROM Clients JOIN Clients_Stylists ON (Clients.id = Clients_Stylists.Client_id) JOIN Stylists ON (Clients_Stylists.Stylist_id = Stylists.id) WHERE Clients.id = :id";
      List<Stylist> Stylists = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetch(Stylist.class);
      return stylists;
    }
  }

}
