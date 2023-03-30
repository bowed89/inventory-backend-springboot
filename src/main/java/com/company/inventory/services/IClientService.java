package com.company.inventory.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.model.Client;
import com.company.inventory.response.ClientResponseRest;

public interface IClientService {
	public ResponseEntity<ClientResponseRest> search();
	public ResponseEntity<ClientResponseRest> deleteById(Long id);
	public ResponseEntity<ClientResponseRest> save(Client client);
	public ResponseEntity<ClientResponseRest> update(Client client, Long id);
	public ResponseEntity<ClientResponseRest> searchbyName(String name);

}
