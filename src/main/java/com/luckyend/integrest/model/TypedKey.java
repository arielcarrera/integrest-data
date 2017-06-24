
package com.luckyend.integrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TypedKey<T,K> {
	
	protected T type;
	
	protected K key;
	
}
