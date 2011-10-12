/*
 * Copyright 2010 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.mgwt.ui.client.widget.base;

import com.google.gwt.user.client.ui.ListBox;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.InputCss;

/**
 * @author Daniel Kurka
 * 
 */
public class MListBox extends ListBox {

	private final InputCss css;

	public MListBox() {
		this(MGWTStyle.getDefaultClientBundle().getInputCss());
	}

	public MListBox(InputCss css) {

		this.css = css;
		this.css.ensureInjected();
		setStylePrimaryName(css.listBox());

	}

}