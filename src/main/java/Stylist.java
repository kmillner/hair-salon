import org.sql2o.*;
import java.util.List;

public class Stylist {

  private int id;
  private String name;
  private String service;

  public Stylist (String name, String service) {
    this.name = name;
    this.service = service;
  }

  public String getName() {
    return name;
  }

  public String getService() {
    return service;
  }


  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherStylistInstance) {
    if (!(otherStylistInstance instanceof Stylist)) {
      return false;
    } else {
      Stylist newStylistInstance = (Stylist) otherStylistInstance;
      return this.getName().equals(newStylistInstance.getName()) &&
             this.getService().equals(newStylistInstance.getService()) &&
             this.getId() == newStylistInstance.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Stylists (name, service) VALUES (:name, :service);";
      this.id = (int) con.createQuery(sql, true)
          .addParameter("name", name)
          .addParameter("service", service)
          .executeUpdate()
          .getKey();
    }
  }

  public void updateName(String name) {
    this.name = name;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE Stylists SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateService(String service) {
    this.service = service;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE Stylists SET service = :service WHERE id = :id";
      con.createQuery(sql)
        .addParameter("service", service)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM Stylists WHERE id = :id";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();
      String joinDeleteQuery = "DELETE FROM Clients_Stylists WHERE Stylist_id = :id";
        con.createQuery(joinDeleteQuery)
          .addParameter("id", id)
          .executeUpdate();
    }
  }

  public static List<Stylist> all() {
    String sql = "SELECT * FROM Stylists";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  public static Stylist find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Stylists WHERE id=:id";
      Stylist Stylist = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Stylist.class);
      return Stylist;
    }
  }
  public void addClient(Client client) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Clients_Stylists (client_id, Stylist_id) VALUES (:client_id, :Stylist_id)";
      con.createQuery(sql)
        .addParameter("client_id", client.getId())
        .addParameter("Stylist_id", id)
        .executeUpdate();
    }
  }

  public List<Client> getClients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT Clients.* FROM Stylists JOIN Clients_Stylists ON (Stylists.id = Clients_Stylists.Stylist_id) JOIN Clients ON (Clients_Stylists.Client_id = Clients.id) WHERE Stylists.id = :id";
      List<Client> Clients = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetch(Client.class);
      return Clients;
    }
  }

}
