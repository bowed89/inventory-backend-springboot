package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

@Service
public class ProductServiceImpl implements IProductService {
	
	//Otra forma de Inyectar ICategoryDao en vez de @Autowired es usando Contructores.
	private ICategoryDao categoryDao;
	private IProductDao productDao;
	
	public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>(); //  se almacena la respuesta cuando se llama al metodo save
		
		try {
			// Buscar Category 
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if(category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata("respuesta nok", "-1", "Categoría no encontrada asociada al producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			// Guardar producto
			Product productSaved = productDao.save(product);
			
			if(productSaved != null) {
				list.add(productSaved);
				response.getProduct().setProducts(list);
				response.setMetadata("respuesta ok", "00", "Producto almacenado");
			} else {
				response.setMetadata("respuesta nok", "-1", "Producto no almacenado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al almacenar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}
	

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchbyId(Long id) {
		
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>(); 
		
		try {
			// Buscar producto por ID
			Optional<Product> product = productDao.findById(id);
			
			if(product.isPresent()) {
				// descomprimimos la img y convertimos en base64 para mostrar al usuario
				/*byte[] imageDescompressed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDescompressed);*/
				
				list.add(product.get());
				response.getProduct().setProducts(list);
				response.setMetadata("respuesta ok", "00", "Producto encontrado");
				
			} else {
				response.setMetadata("respuesta nok", "-1", "Producto no encontrado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al almacenar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchbyName(String name) {
		
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>(); 
		List<Product> listAux = new ArrayList<>(); 

		
		try {
			// Buscar producto por name
			listAux = productDao.findByNameContainingIgnoreCase(name);
			
			if(listAux.size() > 0) {
				//Recorrer la lista
				listAux.stream().forEach((p) -> {
					list.add(p);
				});
				
				response.getProduct().setProducts(list);
				response.setMetadata("respuesta ok", "00", "Productos encontrados");
				
			} else {
				response.setMetadata("respuesta nok", "-1", "Productos no encontrados");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al buscar producto por nombre");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		
		ProductResponseRest response = new ProductResponseRest(); 
		
		try {
			// Eliminar producto por ID
			productDao.deleteById(id);
			response.setMetadata("respuesta ok", "00", "Producto eliminado");

		} catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al eliminar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> search() {
		System.out.println("Entra a get products!");
		ProductResponseRest response = new ProductResponseRest(); 
		List<Product> list = new ArrayList<>();
		
		try {
			System.out.println("Entra try!");

			// Obtener todos los productos
			list = (List<Product>) productDao.findAll();
			response.getProduct().setProducts(list);
			response.setMetadata("respuesta ok", "00", "Respuesta exitosa");
			
		} catch (Exception e) {
			System.out.println("Entra a catch get products!");
			response.setMetadata("Respuesta nok", "-1", "Error al consultar");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
		
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>(); //  se almacena la respuesta cuando se llama al metodo save
		
		try {
			// Buscar Category 
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if(category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata("respuesta nok", "-1", "Categoría no encontrada asociada al producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			// Buscar Producto para actualizar
			Optional<Product> productSearch = productDao.findById(id);
			
			if(productSearch.isPresent()) {
				// setear productos
				productSearch.get().setStock(product.getStock());
				productSearch.get().setCategory(product.getCategory());
				productSearch.get().setName(product.getName());
				productSearch.get().setDescription(product.getDescription());
				productSearch.get().setPicture(product.getPicture());
				productSearch.get().setSale_price(product.getSale_price());
				productSearch.get().setPurchase_price(product.getPurchase_price());
				//guardar producto para actualizat
				Product productToUpdate = productDao.save(productSearch.get());
				
				if(productToUpdate != null) {
					list.add(productToUpdate);
					response.getProduct().setProducts(list);
					response.setMetadata("respuesta ok", "00", "Producto actualizado");					
				} else {
					response.setMetadata("respuesta nok", "-1", "Producto no actualizado");
					return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
				
			} else {
				response.setMetadata("respuesta nok", "-1", "Producto no encontrado para actualizar");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al actualizar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

}
