package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientService {
    private Map<Long, Client> clientsMap;
    private long idCounter;

    public ClientService() {
        this.clientsMap = new HashMap<>();
        this.idCounter = 0;
    }

    public long create(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        Client newClient = new Client(++idCounter, name);
        clientsMap.put(newClient.getId(), newClient);
        return newClient.getId();
    }

    public String getById(long id) {
        Client client = clientsMap.get(id);
        if (client == null) {
            throw new IllegalArgumentException("Client with id " + id + " not found");
        }
        return client.getName();
    }

    public void setName(long id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        Client client = clientsMap.get(id);
        if (client == null) {
            throw new IllegalArgumentException("Client with id " + id + " not found");
        }
        client.setName(name);
    }

    public void deleteById(long id) {
        if (!clientsMap.containsKey(id)) {
            throw new IllegalArgumentException("Client with id " + id + " not found");
        }
        clientsMap.remove(id);
    }

    public List<Client> listAll() {
        return new ArrayList<>(clientsMap.values());
    }
}