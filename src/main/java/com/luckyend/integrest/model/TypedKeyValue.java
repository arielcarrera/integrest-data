
package com.luckyend.integrest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
public class TypedKeyValue<T,K,V> extends TypedKey<T, K> {
	
	protected V value;
	
	public TypedKeyValue(T type, K key, V value){
		super();
		this.type = type;
		this.key = key;
		this.value = value;
	}
}
