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
package org.statefulj.persistence.mongo.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;
import org.statefulj.persistence.mongo.demo.foo.Foo;

public class HelloAction implements Action<Foo> {
	
	Logger logger = LoggerFactory.getLogger(HelloAction.class);

	String what;
	
	public HelloAction(String what) {
		this.what = what;
	}
	
	@Override
	public void execute(
			Foo foo, 
			String event, 
			Object ... args) throws RetryException {
		logger.debug("Hello {}", what);
		logger.debug("Received event={}, for Foo={}", event, foo);
	}

	@Override
	public String toString() {
		return "HelloAction";
	}
}
