package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

}
