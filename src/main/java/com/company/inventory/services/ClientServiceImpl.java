package com.company.inventory.services;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.inventory.dao.IClientDao;
import com.company.inventory.model.Client;
import com.company.inventory.response.ClientResponseRest;

@Service
public class ClientServiceImpl implements IClientService {
	
	@Autowired
	private IClientDao clientDao;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ClientResponseRest> search() {
		ClientResponseRest response = new ClientResponseRest();
		List<Client> list = new ArrayList<>();
		
		try {
			list = (List<Client>) clientDao.findAll();
			response.getClient().setClient(list);
			response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
			
		} catch(Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al consultar");
			e.getStackTrace();
			return new ResponseEntity<ClientResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
		return new ResponseEntity<ClientResponseRest>(response, HttpStatus.OK);
		
	}

	@Override
	@Transactional

	public ResponseEntity<ClientResponseRest> save(Client client) {

		ClientResponseRest response = new ClientResponseRest();
		List<Client> list = new ArrayList<>();
		
		try {
			Client clientSaved = clientDao.save(client);
			
			if(clientSaved != null) {
				list.add(clientSaved);
				response.getClient().setClient(list);
				response.setMetadata("respuesta ok", "00", "Cliente almacenado");

			}
			
		} catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al almacenar producto");
			return new ResponseEntity<ClientResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ClientResponseRest>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ClientResponseRest> update(Client client, Long id) {
		
		ClientResponseRest response = new ClientResponseRest();
		List<Client> list = new ArrayList<>();
		
		try {
			Optional<Client> clientSearch = clientDao.findById(id);
			
			if(clientSearch.isPresent()) {
				clientSearch.get().setName(client.getName());
				clientSearch.get().setLastname(client.getLastname());
				clientSearch.get().setEmail(client.getEmail());
				clientSearch.get().setCi(client.getCi());
				
				Client clientToUpdate = clientDao.save(clientSearch.get());
				
				if(clientToUpdate != null) {
					list.add(clientToUpdate);
					response.getClient().setClient(list);
					response.setMetadata("respuesta ok", "00", "Cliente actualizado");					
				} else {
					response.setMetadata("respuesta nok", "-1", "Producto no actualizado");
					return new ResponseEntity<ClientResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al actualizar cliente");
			return new ResponseEntity<ClientResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ClientResponseRest>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ClientResponseRest> deleteById(Long id) {
		
		ClientResponseRest response = new ClientResponseRest();
		
		try {
			clientDao.deleteById(id);
			response.setMetadata("Respuesta nok", "-1", "Registro eliminado");
			
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
			e.getStackTrace();
			return new ResponseEntity<ClientResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ClientResponseRest>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ClientResponseRest> searchbyName(String name) {
		
		ClientResponseRest response = new ClientResponseRest();
		List<Client> list = new ArrayList<>();
		List<Client> listAux = new ArrayList<>();
		
		try {
			listAux = clientDao.findByNameContainingIgnoreCase(name);
			
			
			if(listAux.size() > 0) {
				listAux.stream().forEach((p) -> {
					list.add(p);
				});
				
				response.getClient().setClient(list);
				response.setMetadata("respuesta ok!", "00", "Clientes encontrados");

			} 
			
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
			e.getStackTrace();
			return new ResponseEntity<ClientResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
		return new ResponseEntity<ClientResponseRest>(response, HttpStatus.OK);
	}

	

}
