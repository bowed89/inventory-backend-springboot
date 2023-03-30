package com.company.inventory.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseRest extends ResponseRest {
	
	private ClientResponse client = new ClientResponse();

}
