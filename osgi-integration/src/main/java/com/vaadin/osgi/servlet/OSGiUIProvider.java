/*
 * Copyright 2000-2016 Vaadin Ltd.
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
package com.vaadin.osgi.servlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.osgi.servlet.OSGiVaadinServlet.LocalVaadinServletService;
import com.vaadin.osgi.servlet.api.Constants;
import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;

/**
 * This implementation of {@link UIProvider} uses {@link ServiceObjects} to
 * create new instances from UIs.
 * 
 * @author Florian Pirchner
 */
@SuppressWarnings("serial")
public class OSGiUIProvider extends UIProvider {

	static final Logger LOGGER = LoggerFactory.getLogger(OSGiUIProvider.class);

	ServiceObjects<UI> serviceObjects;
	Class<UI> uiClass;
	UI ui;

	@SuppressWarnings("unchecked")
	public OSGiUIProvider(ServiceObjects<UI> serviceObjects) {
		super();

		this.serviceObjects = serviceObjects;

		// we need to load the UI class for the ui provider
		Bundle registringBundle = serviceObjects.getServiceReference().getBundle();
		try {
			this.uiClass = (Class<UI>) registringBundle.loadClass(
					(String) serviceObjects.getServiceReference().getProperty(Constants.PROP__VAADIN_UI_CLASS));
		} catch (ClassNotFoundException e) {
			LOGGER.error("{}", e);
		}
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return uiClass;
	}

	@Override
	public UI createInstance(UICreateEvent event) {
		final UI ui = serviceObjects.getService();

		LocalVaadinServletService service = (LocalVaadinServletService) event.getRequest().getService();
		applyPageTitle(service, ui);

		ui.addDetachListener(new DetachListener() {
			@Override
			public void detach(DetachEvent event) {
				serviceObjects.ungetService(ui);
				LOGGER.debug("unregistered UI " + ui.toString());
			}
		});
		return ui;
	}

	private void applyPageTitle(LocalVaadinServletService service, UI ui) {
		String title = service.getConfiguration().pageTitle();
		if (title != null && !title.equals("")) {
			ui.getPage().setTitle(title);
		}
	}

	@Override
	public String getTheme(UICreateEvent event) {
		LocalVaadinServletService service = (LocalVaadinServletService) event.getRequest().getService();
		String theme = service.getConfiguration().theme();
		if (theme == null || theme.equals("")) {
			theme = super.getTheme(event);
		}
		return theme;
	}

	@Override
	public String getPageTitle(UICreateEvent event) {
		LocalVaadinServletService service = (LocalVaadinServletService) event.getRequest().getService();
		String title = service.getConfiguration().pageTitle();
		if (title == null || title.equals("")) {
			title = super.getPageTitle(event);
		}
		return title;
	}

	@Override
	public PushMode getPushMode(UICreateEvent event) {
		LocalVaadinServletService service = (LocalVaadinServletService) event.getRequest().getService();
		com.vaadin.osgi.servlet.PushMode pushMode = service.getConfiguration().pushMode();
		if (pushMode == null) {
			return super.getPushMode(event);
		}
		return PushMode.valueOf(pushMode.name());
	}

	@Override
	public Transport getPushTransport(UICreateEvent event) {
		LocalVaadinServletService service = (LocalVaadinServletService) event.getRequest().getService();
		com.vaadin.osgi.servlet.PushTransport pushTransport = service.getConfiguration().pushTransport();
		if (pushTransport == null) {
			return super.getPushTransport(event);
		}
		return Transport.valueOf(pushTransport.name());
	}

}
