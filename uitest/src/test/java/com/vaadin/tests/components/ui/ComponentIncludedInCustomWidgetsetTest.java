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
package com.vaadin.tests.components.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.tests.tb3.MultiBrowserTest;

/**
 * Tests if a component is included in a custom widgetset
 * (com.vaadin.tests.widgetset.TestingWidgetSet)
 *
 * @author Vaadin Ltd
 */
public class ComponentIncludedInCustomWidgetsetTest extends MultiBrowserTest {

    @Test
    public void testComponentInTestingWidgetsetNotInDefaultWidgetset() {
        openTestURL();
        WebElement component = vaadinElementById("missing-component");
        assertEquals(
                "This component is available in TestingWidgetset, but not in DefaultWidgetset",
                component.getText());
    }
}
