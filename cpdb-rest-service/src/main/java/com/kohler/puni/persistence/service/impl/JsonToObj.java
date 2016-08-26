package com.kohler.puni.persistence.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class JsonToObj {

	public static void main(String[] args) {

		Schema schema = new Schema("NKDR");
		ObjectNode result2 = Json.newObject();
		schema.populate(result2);
		String jString = result2.toString();
		System.out.println(jString);
		JsonNode node = Json.parse(jString);
		Schema schema2 = new Schema();
		schema2.constructFrom(node);

		ObjectNode repopulate = Json.newObject();
		schema2.populate(repopulate);

		System.out.println(repopulate);

	}
}

interface Jsonable {
	public void populate(JsonNode node);

	public void constructFrom(JsonNode node);

	public boolean hasJsonContent();
}

enum ActionType {
	ADD {
		public String getAction() {
			return "Add";
		}
	},
	MODIFY {
		public String getAction() {
			return "MODIFY";
		}
	},
	DELETE {
		public String getAction() {
			return "DELETE";
		}
	};
	public abstract String getAction();

	public static ActionType getType(String action) {
		ActionType actionType = ADD;
		if ("MODIFY".equalsIgnoreCase(action)) {
			actionType = MODIFY;
		}
		if ("DELETE".equalsIgnoreCase(action)) {
			actionType = DELETE;
		}
		if ("ADD".equalsIgnoreCase(action)) {
			actionType = ADD;
		}
		return actionType;
	}
}

class Schema implements Jsonable {
	private String id;
	private String name;
	private List<Action> actions;

	public Schema(String schemaName) {
		this.name = schemaName;
		actions = new ArrayList<Action>();
		actions.add(new Action(ActionType.ADD));
		actions.add(new Action(ActionType.DELETE));
		actions.add(new Action(ActionType.MODIFY));
	}

	public Schema() {
		// TODO Auto-generated constructor stub
	}

	public void populate(JsonNode node) {
		ObjectNode oNode = (ObjectNode) node;
		ObjectNode schemaContainer = Json.newObject();
		oNode.set(name, schemaContainer);
		for (Action action : actions) {
			action.populate(schemaContainer);
		}
	}

	public void constructFrom(JsonNode node) {
		final ObjectNode mainNode = (ObjectNode) node;
		Iterable<String> schemaNames = new Iterable<String>() {
			public Iterator<String> iterator() {
				return mainNode.fieldNames();
			}
		};
		actions = new ArrayList<Action>();
		for (String schemaName : schemaNames) {
			this.name = schemaName;
			actions = new ArrayList<Action>();
			final ObjectNode schNode = (ObjectNode) mainNode.get(schemaName);
			Iterable<String> actNames = new Iterable<String>() {
				public Iterator<String> iterator() {
					return schNode.fieldNames();
				}
			};
			for (String aName : actNames) {
				ObjectNode actNode = (ObjectNode) schNode.get(aName);
				Action action = new Action();
				actions.add(action);
				ObjectNode jsonNode = Json.newObject();
				jsonNode.set(aName, actNode);
				action.constructFrom(jsonNode);
			}

		}

	}

	public boolean hasJsonContent() {
		// TODO Auto-generated method stub
		return false;
	}

}

class Action implements Jsonable {
	private ActionType action;
	private List<Product> products;

	public Action(ActionType action) {
		this.action = action;
		products = new ArrayList<Product>();
		products.add(new Product("P1", "SAMPLE1"));
		products.add(new Product("P2", "SAMPLE2"));
	}

	public Action() {
		// TODO Auto-generated constructor stub
	}

	public void populate(JsonNode node) {
		ObjectNode oNode = (ObjectNode) node;

		ObjectNode prods = Json.newObject();
		oNode.set(action.getAction(), prods);
		for (Product product : products) {
			product.populate(prods);
		}
	}

	public void constructFrom(JsonNode node) {
		final ObjectNode mainNode = (ObjectNode) node;
		Iterable<String> actNames = new Iterable<String>() {
			public Iterator<String> iterator() {
				return mainNode.fieldNames();
			}
		};
		for (String aName : actNames) {
			this.action = ActionType.getType(aName);
			final ObjectNode csNode = (ObjectNode) mainNode.get(aName);
			products = new ArrayList<Product>();
			Iterable<String> names = new Iterable<String>() {
				public Iterator<String> iterator() {
					return csNode.fieldNames();
				}
			};
			for (String pName : names) {
				ObjectNode aNode = (ObjectNode) csNode.get(pName);
				Product product = new Product();
				products.add(product);
				ObjectNode newPNode = Json.newObject();
				newPNode.set(pName, aNode);
				product.constructFrom(newPNode);
			}

		}

	}

	public boolean hasJsonContent() {
		// TODO Auto-generated method stub
		return false;
	}

}

class Product implements Jsonable {
	private String id;
	private String name;
	List<CrossSell> list;

	public Product(String id, String name) {
		this.id = id;
		this.name = name;
		list = new ArrayList<CrossSell>();
		list.add(new CrossSell(id + "1", name + "CS1"));
		list.add(new CrossSell(id + "2", name + "CS2"));
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public void populate(JsonNode node) {
		ObjectNode oNode = (ObjectNode) node;
		ObjectNode csContainer = Json.newObject();
		ObjectNode cs = Json.newObject();

		oNode.set(name, csContainer);

		csContainer.set("crossSelling", cs);
		for (CrossSell crossSell : list) {
			crossSell.populate(cs);
		}
	}

	public boolean hasJsonContent() {
		return (list != null && list.size() > 0);
	}

	public void constructFrom(JsonNode node) {
		final ObjectNode mainNode = (ObjectNode) node;

		Iterable<String> prodNames = new Iterable<String>() {
			public Iterator<String> iterator() {
				return mainNode.fieldNames();
			}
		};
		for (String pName : prodNames) {
			this.name = pName;
			final ObjectNode oNode = (ObjectNode) mainNode.get(name);
			final ObjectNode csNode = (ObjectNode) oNode.get("crossSelling");
			list = new ArrayList<CrossSell>();
			Iterable<String> names = new Iterable<String>() {
				public Iterator<String> iterator() {
					return csNode.fieldNames();
				}
			};
			for (String fName : names) {
				ArrayNode aNode = (ArrayNode) csNode.get(fName);
				CrossSell crossSell = new CrossSell();
				list.add(crossSell);
				ObjectNode newPNode = Json.newObject();
				newPNode.set(fName, aNode);
				crossSell.constructFrom(newPNode);
			}

		}

	}
}

class CrossSell implements Jsonable {
	private String id;
	private String name;
	private List<Item> items;

	public CrossSell(String id, String name) {
		this.id = id;
		this.name = name;
		items = new ArrayList<Item>();
		items.add(new Item(id + "IT1", name + "ITEM1"));
		items.add(new Item(id + "IT2", name + "ITEM2"));

	}

	public CrossSell() {
		// TODO Auto-generated constructor stub
	}

	public void populate(JsonNode node) {
		ObjectNode oNode = (ObjectNode) node;
		ArrayNode itemsCont = Json.newArray();
		oNode.set(name, itemsCont);
		for (Item item : items) {
			item.populate(itemsCont);
		}
	}

	public void constructFrom(JsonNode node) {
		final ObjectNode oNode = (ObjectNode) node;
		items = new ArrayList<Item>();
		Iterable<String> names = new Iterable<String>() {
			public Iterator<String> iterator() {
				return oNode.fieldNames();
			}
		};
		for (String fName : names) {
			this.name = fName;
			ArrayNode aNode = (ArrayNode) oNode.get(fName);
			for (JsonNode jsonNode : aNode) {
				Item item = new Item();
				items.add(item);
				item.constructFrom(jsonNode);
			}
		}
		// TODO Auto-generated method stub

	}

	public boolean hasJsonContent() {
		return (items != null && items.size() > 0);
	}
}

class Item implements Jsonable {
	private String id;
	private String name;

	public Item() {
	}

	public Item(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public void populate(JsonNode node) {
		ArrayNode aNode = (ArrayNode) node;
		aNode.add(name);
	}

	public void constructFrom(JsonNode node) {
		name = node.asText();
	}

	public boolean hasJsonContent() {
		return true;
	}
}