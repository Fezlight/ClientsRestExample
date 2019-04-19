package fr.eni.javaee.bo;

public class Client {
	private String name;
	private Integer id;
	private static int lastId = 1;

	public Client(String name) {
		this.id = lastId++;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
