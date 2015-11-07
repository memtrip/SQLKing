/**
 * Copyright 2013-present beyond constraint.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beyondconstraint.sqlking.schema;

/**
 * An object representation of a SQLite column
 * @author samkirton
 */
public class Column {
	private String mName;
	private String mType;
	private String mValue;
	
	public String getName() {
		return mName;
	}
	
	public void setName(String newVal) {
		mName = newVal;
	}
	
	public String getType() {
		return mType;
	}
	
	public void setType(String newVal) {
		mType = newVal;
	}
	
	public String getValue() {
		return mValue;
	}
	
	public void setValue(String newVal) {
		mValue = newVal;
	}
}