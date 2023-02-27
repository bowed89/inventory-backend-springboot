package com.company.inventory.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductService;
import com.company.inventory.util.CategoryExcelExporter;
import com.company.inventory.util.ProductExcelExporter;
import com.company.inventory.util.Util;

import org.apache.tomcat.util.codec.binary.Base64;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {
	
	// Inyeccion de dependencia mediante constructor
	private IProductService productService;
	public ProductRestController(IProductService productService) {
		super();
		this.productService = productService;
	}

	/**
	 * Añadir nuevo producto
	 * @param picture
	 * @param name
	 * @param price
	 * @param account
	 * @param categoryId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
				@RequestParam("picture") MultipartFile picture,
				@RequestParam("name") String name,
				@RequestParam("price") int price,
				@RequestParam("account") int account,
				@RequestParam("categoryId") Long categoryId) throws IOException
	{
		Product product = new Product();
		product.setName(name);
		product.setAccount(account);
		product.setPrice(price);
        byte[] image = Base64.encodeBase64(picture.getBytes());
		//byte[] image = Util.compressZLib(picture.getBytes());
		String result = new String(image);
		product.setPicture(result);
		
		System.out.print(product);
		
		ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);
		
		return response;
		
	}

	/**
	 * Buscar por ID
	 * @param id
	 * @return
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> searchById(@PathVariable Long id) {
		ResponseEntity<ProductResponseRest> response = productService.searchbyId(id);
		return response;
	}
	
	/**
	 * Buscar por nombre
	 * @param name
	 * @return
	 */
	@GetMapping("/products/filter/{name}")
	public ResponseEntity<ProductResponseRest> searchByName(@PathVariable String name) {
		ResponseEntity<ProductResponseRest> response = productService.searchbyName(name);
		return response;
	}

	/**
	 * Eliminar por ID
	 * @param id
	 * @return
	 */
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> deleteById(@PathVariable Long id) {
		ResponseEntity<ProductResponseRest> response = productService.deleteById(id);
		return response;
	}
	
	/**
	 * Obtener todos los productos
	 * @return
	 */
	@GetMapping("/products")
	public ResponseEntity<ProductResponseRest> search() {
		ResponseEntity<ProductResponseRest> response = productService.search();
		return response;
	}
	
	/**
	 * Actualizar Producto
	 * @param picture
	 * @param name
	 * @param price
	 * @param account
	 * @param categoryId
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> update(
				@RequestParam("picture") MultipartFile picture,
				@RequestParam("name") String name,
				@RequestParam("price") int price,
				@RequestParam("account") int account,
				@RequestParam("categoryId") Long categoryId,
				@PathVariable Long id) throws IOException
	{
		Product product = new Product();
		product.setName(name);
		product.setAccount(account);
		product.setPrice(price);
		
		byte[] image = Base64.encodeBase64(picture.getBytes());
		String result = new String(image);
		product.setPicture(result);
		ResponseEntity<ProductResponseRest> response = productService.update(product, categoryId, id);
		
		return response;
		
	}
	
	/**
	 * Exportar a Archivo EXCEL
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/products/export/excel")
	public void exportToExcel(HttpServletResponse response)throws IOException {
		response.setContentType("application/octet-stream"); // representa un archivo Excel
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=result_product";
		response.setHeader(headerKey, headerValue);
		
		ResponseEntity<ProductResponseRest> productResponse = productService.search();
		
		ProductExcelExporter excelExporter = new ProductExcelExporter(
				productResponse.getBody().getProduct().getProducts());
		
		excelExporter.export(response);

	}
	
}
