import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import static java.lang.System.out;
import java.lang.*;

import java.util.Map;
import java.util.HashMap;

import spark.ModelAndView;

import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("stylists", Stylist.all());
      model.put("template", "templates/index.vtl");

      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients",(request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");
      int stylist_id = Integer.parseInt(request.queryParams("stylist_id"));
      Client newClient = new Client(name, stylist_id);

      newClient.save();

      model.put("client", newClient);
      model.put("template", "templates/index.vtl");
      response.redirect("/");
      return null;
    });

    post("/stylists", (request,response)-> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");
      String services = request.queryParams("services");

      Stylist newStylist = new Stylist(name, services);

      newStylist.save();

      model.put("stylist", newStylist);
      model.put("template", "templates/index.vtl");
      response.redirect("/");
      return null;
    });

    get("/stylists/:id", (request, response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Stylist stylist = Stylist.find(id);
      Client client = Client.find(id);

      model.put("stylist", stylist);
      model.put("client", client);
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:client_id/stylists/:stylist_id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      int clientId = Integer.parseInt(request.params(":client_id"));
      Client myClient = Client.find(clientId);

      int stylistId = Integer.parseInt(request.params(":stylist_id"));
      Stylist stylistToDelete = Stylist.find(stylistId);

      stylistToDelete.delete();

      response.redirect("/stylists/" + request.params(":client_id"));
      return null;
    });

  }
}
