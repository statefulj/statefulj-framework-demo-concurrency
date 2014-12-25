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
package org.statefulj.persistence.mongo.demo.foo;

import org.springframework.data.annotation.Id;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.mongo.model.StatefulDocument;

public class Foo extends StatefulDocument {
	
	// Events
	//
	public final static String eventA = "Event A";
	public final static String eventB = "Event B";
	public final static String eventC = "Event C";

	// States
	//
	public final static StateImpl<Foo> stateA = new StateImpl<Foo>("State A");
	public final static StateImpl<Foo> stateB = new StateImpl<Foo>("State B");
	public final static StateImpl<Foo> stateC = new StateImpl<Foo>("State C", true); // End State
	
	@Id
	private String id;

	private boolean bar;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public boolean isBar() {
        return bar;
    }
    
    public String toString() {
    	return "Foo[id=" + id + ", state=" + this.getStateDocument().getState() + ", bar=" + bar + "]";
    }
}
