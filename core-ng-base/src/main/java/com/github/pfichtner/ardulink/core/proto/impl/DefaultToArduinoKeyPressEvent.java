/**
Copyright 2013 project Ardulink http://www.ardulink.org/
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.github.pfichtner.ardulink.core.proto.impl;

import com.github.pfichtner.ardulink.core.proto.api.ToArduinoKeyPressEvent;

public class DefaultToArduinoKeyPressEvent implements ToArduinoKeyPressEvent {

	private final char keychar;
	private final int keycode;
	private final int keylocation;
	private final int keymodifiers;
	private final int keymodifiersex;

	public DefaultToArduinoKeyPressEvent(char keychar, int keycode, int keylocation,
			int keymodifiers, int keymodifiersex) {
		this.keychar = keychar;
		this.keycode = keycode;
		this.keylocation = keylocation;
		this.keymodifiers = keymodifiers;
		this.keymodifiersex = keymodifiersex;
	}

	@Override
	public char getKeychar() {
		return keychar;
	}

	@Override
	public int getKeycode() {
		return keycode;
	}

	@Override
	public int getKeylocation() {
		return keylocation;
	}

	@Override
	public int getKeymodifiers() {
		return keymodifiers;
	}

	@Override
	public int getKeymodifiersex() {
		return keymodifiersex;
	}

}