package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServiceImpl implements ICategoryService{
	
	@Autowired
	private ICategoryDao categoryDao;

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> search() {
		
		CategoryResponseRest response = new CategoryResponseRest();
		
		try {
			List<Category> category = (List<Category>) categoryDao.findAll();
			//lleno el metodo de categorias y seteo la lista q obtuve con todas las categorias
			response.getCategoryResponse().setCategory(category); 
			response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al consultar");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			// devuelve un objeto optional
			Optional<Category> category = categoryDao.findById(id);
			if(category.isPresent()) {
				list.add(category.get());
				// luego seteamos a la respuesta
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta ok", "00", "Categoria encontrada");

			} else {
				response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al consultar por ID");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			Category categorySaved = categoryDao.save(category);
			
			if(categorySaved != null) {
				list.add(categorySaved);
				// luego seteamos a la respuesta
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta ok", "00", "Categoria almacenada");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Categoria no almacenada");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al almacenar categoria");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			
			Optional<Category> categorySearch = categoryDao.findById(id);
			
			if(categorySearch.isPresent()) {
				// Si no esta vacio, se actualiza
				categorySearch.get().setName(category.getName());
				categorySearch.get().setDescription(category.getDescription());
				
				Category categoryToUpdate = categoryDao.save(categorySearch.get());
				
				if(categoryToUpdate != null) {
					list.add(categoryToUpdate);
					response.getCategoryResponse().setCategory(list);
					response.setMetadata("Respuesta ok", "00", "Categoria actualizada");
					
				} else {
					response.setMetadata("Respuesta nok", "-1", "Categoria no actualizada");
					return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
				}
				
			} else {
				response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al actualizar categoria");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> deleteById(Long id) {

		CategoryResponseRest response = new CategoryResponseRest();
		
		try {
			categoryDao.deleteById(id);
			response.setMetadata("respuesta ok", "00", "Registro eliminado");

			
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}
		

}
