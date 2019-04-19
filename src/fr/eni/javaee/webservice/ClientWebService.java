package fr.eni.javaee.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.eni.javaee.bll.ClientManager;
import fr.eni.javaee.bo.Client;

@ApplicationPath("/clientApp")
@Path("/clients")
public class ClientWebService extends Application{
	ClientManager clientManager = ClientManager.getInstance();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getClients() {
		Map<String, Object> datas = new HashMap<>();
		datas.put("customers", this.getResultClient(clientManager.getClients()));
		return this.getResult(true, "Voici la liste des clients", datas);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id : \\d+}")
	public String getClientById(@PathParam("id") int id) {
		Map<String, Object> datas = new HashMap<>();
		Client client = this.clientManager.getClientById(id);
		if(client == null) {
			return this.getResult(false, "Pas de client avec l'id "+ id, datas);
		}
		datas.put("customers", this.getResultClient(client));
		return this.getResult(true, "Voici le client avec l'id " + id, datas);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{name: [a-zA-Z0-9_/]+}")
	public String getClientByName(@PathParam("name") String name) {
		Map<String, Object> datas = new HashMap<>();
		Client client = this.clientManager.getClientByName(name);
		if(client == null) {
			return this.getResult(false, "Pas de client avec le nom "+ name, datas);
		}
		datas.put("customers", this.getResultClient(client));
		return this.getResult(true, "Voici le client avec le nom "+ name, datas);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String addClient(@FormParam("name") String name) {
		Map<String, Object> datas = new HashMap<>();
		Client client = new Client(name);
		clientManager.addClient(client);
		return this.getResult(true, "Ajout effectu� avec succ�s",datas);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id : \\d+}")
	public String updateClient(@PathParam("id") int id,
								@FormParam("name") String name) {
		Map<String, Object> datas = new HashMap<>();
		Client client = this.clientManager.getClientById(id);
		if(name == null || name.equalsIgnoreCase("")) {
			return this.getResult(false, "Il manque le param�tre name", datas);
		}
		if(client == null) {
			return this.getResult(false, "Pas de client avec l'id "+ id, datas);
		}
		client.setName(name);
		return this.getResult(true, "Modification effectu� avec succ�s",datas);
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id : \\d+}")
	public String deleteClient(@PathParam("id") int id) {
		Map<String, Object> datas = new HashMap<>();
		this.clientManager.deleteClient(id);
		return this.getResult(true, "Suppression effectu� avec succ�s",datas);
	}

	public JsonArray getResultClient(Client client) {
		List<Client> clients = new ArrayList<>();
		clients.add(client);
		return this.getResultClient(clients);
	}
	
	public JsonArray getResultClient(List<Client> clients) {
		JsonArray jsonArray = new JsonArray();
		clients.forEach(client -> {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", client.getId());
			jsonObject.addProperty("name", client.getName());
			jsonArray.add(jsonObject);
		});
		return jsonArray;
	}

	public String getResult(boolean success, String msg, Map<String, Object> data) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("success", success);
		jsonObject.addProperty("msg", msg);
		
		for(Entry<String, Object> dataSet: data.entrySet()) {
			if(dataSet.getValue() instanceof String) jsonObject.addProperty(dataSet.getKey(), (String)dataSet.getValue());
			if(dataSet.getValue() instanceof Number) jsonObject.addProperty(dataSet.getKey(), (Number)dataSet.getValue());
			if(dataSet.getValue() instanceof Boolean) jsonObject.addProperty(dataSet.getKey(), (Boolean)dataSet.getValue());
			if(dataSet.getValue() instanceof Character) jsonObject.addProperty(dataSet.getKey(), (Character)dataSet.getValue());
			if(dataSet.getValue() instanceof JsonElement) jsonObject.add(dataSet.getKey(), (JsonElement)dataSet.getValue());
		}
		
		return jsonObject.toString();
	}
}

