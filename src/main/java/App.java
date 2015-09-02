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
      CLient newClient = new Client(name, stylist_id);

      newClient.save();

      //Category.find(category_id).addTask(newTask);
      model.put("client", newClient);
      //model.put("tasks", Task.all());
      model.put("template", "templates/index.vtl");
      response.redirect("/");
      return null;
    });

    post("/stylists", (request,response)-> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");
      String services = request.queryParams("services");
      int rate = Integer.parseInt(request.queryParams("rate"));


      Stylist newStylist = new Stylist(name, services, rate);

      newStylist.save();

      model.put("stylist", newStylist);
      //model.put("tasks", Task.all());
      model.put("template", "templates/index.vtl");
      response.redirect("/");
      return null;
    });

    get("/stylists/:id", (request, response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Stylist stylist = Stylist.find(id);

      model.put("stylist", stylist);
      model.put("clients", clients);
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:category_id/tasks/:task_id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      int catId = Integer.parseInt(request.params(":category_id"));
      Category myCategory = Category.find(catId);

      int taskId = Integer.parseInt(request.params(":task_id"));
      Task taskToDelete = Task.find(taskId);

      taskToDelete.delete();

      response.redirect("/stylists/" + request.params(":category_id"));
      return null;
    });

    post("/stylists/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));

      category.delete();
      response.redirect("/");
      return null;
    });


  }
}
