
package com.luckyend.integrest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TestData {

	private String data;
	
	private DataTypeEnum type = DataTypeEnum.JSON;
	
	private String description;

}
