/***
 * 
 * Copyright 2014 Andrew Hall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.statefulj.concurrency;

import javax.annotation.Resource;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

/**
 * @author Andrew Hall
 *
 */
@Component
public class UnsafeValueDocument {
	
	public final static String ID = "1";
	
	@Id
	private String id = ID;
	
	@Resource
	@Transient
	private UnsafeValueDocumentRepository valueRepository;
	
	private int value;
	
	public void increment() {
		this.value += 1;
		valueRepository.save(this);
	}

	public String getId() {
		return this.id;
	}
	
	public int getValue() {
		return this.value;
	}
	
}
