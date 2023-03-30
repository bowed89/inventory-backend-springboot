package com.company.inventory.response;

import java.util.List;
import com.company.inventory.model.Client;
import lombok.Data;

@Data
public class ClientResponse {
	
	private List<Client> client;
	
}
