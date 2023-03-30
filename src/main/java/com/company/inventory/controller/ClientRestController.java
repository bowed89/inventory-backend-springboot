package com.company.inventory.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.model.Client;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.response.ClientResponseRest;
import com.company.inventory.services.IClientService;
import com.company.inventory.util.ClientExcelExporter;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1")
public class ClientRestController {
	
	@Autowired
	private IClientService service;
	
	/**
	 * Obtener todas los clientes
	 * @return
	 */
	@GetMapping("/clients")
	public ResponseEntity<ClientResponseRest> searchClient() {
		ResponseEntity<ClientResponseRest> response = service.search();
		return response;
	}
	
	/**
	 * agregar nuevo cliente
	 * @param client
	 * @return
	 */
	@PostMapping("/clients")
	public ResponseEntity<ClientResponseRest> save(
			@RequestBody Client client) {
		ResponseEntity<ClientResponseRest> response = service.save(client);
		return response;
	}
	
	/**
	 * Actualizar cliente
	 * @param client
	 * @param id
	 * @return
	 */
	@PutMapping("/clients/{id}")
	public ResponseEntity<ClientResponseRest> update(
			@RequestBody Client client, 
			@PathVariable Long id) {
		ResponseEntity<ClientResponseRest> response = service.update(client, id);
		return response;
		
	}
	
	/**
	 * Eliminar cliente por ID
	 * @param id
	 * @return
	 */
	@DeleteMapping("/clients/{id}")
	public ResponseEntity<ClientResponseRest> delete(@PathVariable Long id) {
		ResponseEntity<ClientResponseRest> response = service.deleteById(id);
		return response;
	}
	
	/**
	 * Exportar a Archivo EXCEL
	 * 
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/clients/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream"); // representa un archivo Excel

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=result_product";
		response.setHeader(headerKey, headerValue);

		ResponseEntity<ClientResponseRest> clientResponse = service.search();

		ClientExcelExporter excelExporter = new ClientExcelExporter(
				clientResponse.getBody().getClient().getClient());

		excelExporter.export(response);

	}
	
	/**
	 * Obtener cliente por nombre
	 * @param name
	 * @return
	 */
	@GetMapping("/clients/filter/{name}")
	public ResponseEntity<ClientResponseRest> searchByName(@PathVariable String name) {
		ResponseEntity<ClientResponseRest> response = service.searchbyName(name);
		return response;
	}
}
