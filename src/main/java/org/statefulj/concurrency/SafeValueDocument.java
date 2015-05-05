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
import org.statefulj.framework.core.annotations.FSM;
import org.statefulj.framework.core.annotations.StatefulController;
import org.statefulj.framework.core.annotations.Transition;
import org.statefulj.framework.core.model.StatefulFSM;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.persistence.mongo.model.StatefulDocument;

import static org.statefulj.concurrency.SafeValueDocument.*;

/**
 * @author Andrew Hall
 *
 */
@StatefulController(
	clazz=SafeValueDocument.class,
	startState=READY,
	retryAttempts=-1,
	retryInterval=2,
	blockingStates={LOCKED},
	noops={
		@Transition(from=LOCKED, event=UNLOCK, to=READY)
	}
)
public class SafeValueDocument extends StatefulDocument {
	
	public final static String ID = "1";
	
	// States
	//
	public final static String READY = "ready";
	public final static String LOCKED = "locked";

	// Events
	//
	public final static String INCREMENT = "increment";
	public final static String UNLOCK = "unlock";

	@Id
	private String id = ID;
	
	@FSM
	@Transient
	private StatefulFSM<SafeValueDocument> fsm;

	@Resource
	@Transient
	private SafeValueDocumentRepository valueRepository;
	
	private int value;
	
	public void increment() {
		try {
			fsm.onEvent(this, INCREMENT);
		} catch (TooBusyException e) {
			throw new RuntimeException(e);
		}
	}

	public String getId() {
		return this.id;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Transition(from=READY, event=INCREMENT, to=LOCKED, reload=true)
	private String doIncrement() {
		this.value += 1;
		valueRepository.save(this);
		return "event:" + UNLOCK;
	}
}
